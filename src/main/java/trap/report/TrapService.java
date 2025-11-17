package trap.report;

import trap.common.EventTypes;
import trap.model.IndividualTotal;
import trap.model.RoundScore;
import trap.model.RoundTotal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class TrapService {
    protected static final Map<String, Integer> ROUND_COUNTS = determineRoundsToCount();
    private static final Map<String, Integer> EVENT_COUNTS = determineEventsToCount();
    private static final int MIN_UNIQUE_LOCATIONS = 3;

    private static Map<String, Integer> determineEventsToCount() {
        var eventCounts = new HashMap<String, Integer>();
        eventCounts.put(EventTypes.SINGLES, 5);
        eventCounts.put(EventTypes.DOUBLES, 5);
        eventCounts.put(EventTypes.HANDICAP, 5);
        eventCounts.put(EventTypes.SKEET, 5);
        eventCounts.put(EventTypes.CLAYS, 4);
        eventCounts.put(EventTypes.FIVESTAND, 5);
        eventCounts.put(EventTypes.DOUBLESKEET, 5);
        return eventCounts;
    }

    public static int getEventsToCount(String type) {
        return EVENT_COUNTS.getOrDefault(type, 0); // Default to 0 if type not found
    }

    public static String trimString(String s) {
        return s.trim();
    }

    private static Map<String, Integer> determineRoundsToCount() {
        var roundCounts = new HashMap<String, Integer>();
        roundCounts.put(EventTypes.SINGLES, 5);
        roundCounts.put(EventTypes.DOUBLES, 5);
        roundCounts.put(EventTypes.HANDICAP, 5);

        roundCounts.put(EventTypes.SKEET, 3);
        roundCounts.put(EventTypes.CLAYS, 3);
        roundCounts.put(EventTypes.FIVESTAND, 3);
        roundCounts.put(EventTypes.DOUBLESKEET, 3);
        return roundCounts;
    }

    public static int getRoundsToCount(String type) {
        return ROUND_COUNTS.getOrDefault(type, 0); // Default to 0 if type not found
    }

    public static int parseInteger(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException _) {
            return 0;
        }
    }

    public static int setStringToZero(String number) {
        return number.isEmpty() ? 0 : parseInteger(number);
    }

    public Map<String, List<RoundTotal>> calculatePlayerRoundTotals(List<RoundScore> roundScores) {
        Map<String, List<RoundTotal>> playerRoundTotals = new HashMap<>();

        // Initialize map with empty lists for each unique player
        roundScores.forEach(r -> playerRoundTotals.put(r.uniqueName(), new ArrayList<>()));

        // Process each round score
        for (RoundScore r : roundScores) {
            List<RoundTotal> currentPlayerRoundTotal = playerRoundTotals.get(r.uniqueName());

            // If it's a single round, process round 1 and optionally round 2
            if (singleRound(r.type())) {
                addRound(currentPlayerRoundTotal, r, r.round1());
                if (r.round2() > 0) {
                    addRound(currentPlayerRoundTotal, r, r.round2());
                }
            } else {
                addRound(currentPlayerRoundTotal, r, r.round1() + r.round2());
                addMultipleRounds(currentPlayerRoundTotal, r);
            }
        }

        return playerRoundTotals;
    }

    // Helper method to add individual rounds
    private void addRound(List<RoundTotal> totals, RoundScore r, int total) {
        totals.add(new RoundTotal(r.eventId(), r.locationId(), r.team(), r.athlete(),
                r.classification(), r.gender(), total, r.type()));
    }

    // Helper method to add rounds 3 to 8 if applicable
    private void addMultipleRounds(List<RoundTotal> totals, RoundScore r) {
        int[] additionalRounds = {r.round3() + r.round4(), r.round5() + r.round6(), r.round7() + r.round8()};

        for (int round : additionalRounds) {
            if (round > 0) {
                addRound(totals, r, round);
            } else {
                break; // Stop processing once a round with zero total is found
            }
        }
    }

    public Map<String, List<IndividualTotal>> calculatePlayerIndividualTotal(List<RoundScore> roundScores, Map<String, List<RoundTotal>> playerRoundTotals) {
        Map<String, List<IndividualTotal>> playerIndividualTotal = new HashMap<>();

        // Initialize scores with empty lists
        roundScores.forEach(r -> playerIndividualTotal.put(r.uniqueName(), new ArrayList<>()));

        for (List<RoundTotal> playerRoundTotal : playerRoundTotals.values()) {
            if (playerRoundTotal.isEmpty()) continue;

            String playerName = playerRoundTotal.getFirst().uniqueName();
            int roundsToCount = getEventsToCount(playerRoundTotal.getFirst().type());

            // Sort in descending order based on total score
            playerRoundTotal.sort(Comparator.comparingInt(RoundTotal::total).reversed());

            List<IndividualTotal> finalList = new ArrayList<>();
            Set<Integer> foundLocationIds = new HashSet<>();
            List<IndividualTotal> nonUniqueScores = new ArrayList<>();

            // Find the best score from up to MIN_UNIQUE_LOCATIONS
            for (RoundTotal roundTotal : playerRoundTotal) {
                if (foundLocationIds.size() < MIN_UNIQUE_LOCATIONS) {
                    // Check if this score is from a new location
                    if (!foundLocationIds.contains(roundTotal.locationId())) {
                        finalList.add(toIndividualTotal(roundTotal)); // Add it to our core list
                        foundLocationIds.add(roundTotal.locationId());
                    } else {
                        // We've already got a score from this location.
                        // Add it to the "remainder" pile for Pass 2.
                        nonUniqueScores.add(toIndividualTotal(roundTotal));
                    }
                } else {
                    // We have found our 3 unique locations.
                    // Add this score (and all remaining) to the remainder pile.
                    nonUniqueScores.add(toIndividualTotal(roundTotal));
                }
            }

            int totalUniqueCount = foundLocationIds.size();
            int targetSize;

            if (totalUniqueCount >= MIN_UNIQUE_LOCATIONS) {
                // We found 3+ unique locations. Our target of scores is roundsToCount
                targetSize = roundsToCount;
            } else {
                // Only found 1 or 2 unique locations in the whole list, decrease target size.
                int deficit = MIN_UNIQUE_LOCATIONS - totalUniqueCount;
                targetSize = roundsToCount - deficit;
            }

            // --- PASS 2: Fill the list to the target size ---
            int scoresNeeded = targetSize - finalList.size();
            if (scoresNeeded > 0) {
                finalList.addAll(nonUniqueScores.stream().limit(scoresNeeded).toList());
            }

            // Ensure the final list is returned in sorted order
            finalList.sort(Comparator.comparingInt(IndividualTotal::total).reversed());

            playerIndividualTotal.put(playerName, finalList);
        }

        return playerIndividualTotal;
    }

    private IndividualTotal toIndividualTotal(RoundTotal t) {
        return new IndividualTotal(t.locationId(), t.team(), t.athlete(), t.classification(), t.gender(), t.total(), t.type());
    }

    public Map<String, IndividualTotal> calculatePlayerFinalTotal(Map<String, List<IndividualTotal>> playerIndividualTotals) {
        var playerFinalTotals = new HashMap<String, IndividualTotal>();
        playerIndividualTotals.forEach((key, totals) -> {
            if (!totals.isEmpty()) {
                var firstTotal = totals.getFirst();
                int totalScore = totals.stream().mapToInt(IndividualTotal::total).sum();

                playerFinalTotals.put(key, new IndividualTotal(
                        0,
                        firstTotal.team(),
                        firstTotal.athlete(),
                        firstTotal.classification(),
                        firstTotal.gender(),
                        totalScore,
                        firstTotal.type()
                ));
            }
        });

        return playerFinalTotals;
    }

    public boolean singleRound(String roundType) {
        Set<String> validRounds = Set.of(EventTypes.CLAYS, EventTypes.DOUBLES, EventTypes.DOUBLESKEET, EventTypes.FIVESTAND);
        return validRounds.contains(roundType);
    }
}
