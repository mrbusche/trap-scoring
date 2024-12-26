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
    protected static final Map<String, Integer> roundCounts = determineRoundsToCount();
    private static final Map<String, Integer> eventCounts = determineEventsToCount();

    private static Map<String, Integer> determineEventsToCount() {
        var eventCounts = new HashMap<String, Integer>();
        eventCounts.put(EventTypes.SINGLES, 4);
        eventCounts.put(EventTypes.DOUBLES, 4);
        eventCounts.put(EventTypes.HANDICAP, 4);
        eventCounts.put(EventTypes.SKEET, 4);
        eventCounts.put(EventTypes.CLAYS, 3);
        eventCounts.put(EventTypes.FIVESTAND, 4);
        eventCounts.put(EventTypes.DOUBLESKEET, 4);
        return eventCounts;
    }

    public static int getEventsToCount(String type) {
        return eventCounts.getOrDefault(type, 0); // Default to 0 if type not found
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
        return roundCounts.getOrDefault(type, 0); // Default to 0 if type not found
    }

    public static int parseInteger(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
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
            List<IndividualTotal> indTotal = new ArrayList<>();

            // Sort in descending order based on total score
            playerRoundTotal.sort(Comparator.comparingInt(RoundTotal::total).reversed());

            Set<Integer> locationIds = new HashSet<>();
            for (RoundTotal t : playerRoundTotal) {
                if (indTotal.size() < roundsToCount - 1) {
                    // Add round directly until we reach the number of rounds to count
                    indTotal.add(toIndividualTotal(t));
                    locationIds.add(t.locationId());
                } else if (shouldAddRound(t, locationIds)) {
                    // Handle special case when location constraints are applied
                    indTotal.add(toIndividualTotal(t));
                    break;
                }
            }

            playerIndividualTotal.put(playerName, indTotal);
        }

        return playerIndividualTotal;
    }

    private IndividualTotal toIndividualTotal(RoundTotal t) {
        return new IndividualTotal(t.locationId(), t.team(), t.athlete(), t.classification(), t.gender(), t.total(), t.type());
    }

    // Method to determine if a round should be added based on location constraints
    private boolean shouldAddRound(RoundTotal t, Set<Integer> locationIds) {
        int locationId = t.locationId();
        // Check if the location doesn't already exist
        if (!locationIds.contains(locationId)) {
            locationIds.add(locationId);
            return true;
        }
        // If there are already 2 or 3 unique locations, consider adding the round
        return locationIds.size() == 2 || locationIds.size() == 3;
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
