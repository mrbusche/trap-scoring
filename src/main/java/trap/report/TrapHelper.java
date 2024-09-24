package trap.report;

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

public class TrapHelper {
    private static final Map<String, Integer> roundCounts = determineEventsToCount();

    public Map<String, List<RoundTotal>> calculatePlayerRoundTotals(List<RoundScore> roundScores) {
        Map<String, List<RoundTotal>> playerRoundTotals = new HashMap<>();

        // Initialize map with empty lists for each unique player
        roundScores.forEach(r -> playerRoundTotals.put(r.getUniqueName(), new ArrayList<>()));

        // Process each round score
        for (RoundScore r : roundScores) {
            List<RoundTotal> currentPlayerRoundTotal = playerRoundTotals.get(r.getUniqueName());

            // If it's a single round, process round 1 and optionally round 2
            if (singleRound(r.getType())) {
                addRound(currentPlayerRoundTotal, r, r.getRound1());
                if (r.getRound2() > 0) {
                    addRound(currentPlayerRoundTotal, r, r.getRound2());
                }
            } else {
                addRound(currentPlayerRoundTotal, r, r.getRound1() + r.getRound2());
                addMultipleRounds(currentPlayerRoundTotal, r);
            }
        }

        return playerRoundTotals;
    }

    // Helper method to add individual rounds
    private void addRound(List<RoundTotal> totals, RoundScore r, int total) {
        totals.add(new RoundTotal(r.getEventId(), r.getLocationId(), r.getTeam(), r.getAthlete(),
                r.getClassification(), r.getGender(), total, r.getType()));
    }

    // Helper method to add rounds 3 to 8 if applicable
    private void addMultipleRounds(List<RoundTotal> totals, RoundScore r) {
        int[] additionalRounds = {r.getRound3() + r.getRound4(), r.getRound5() + r.getRound6(), r.getRound7() + r.getRound8()};

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
        roundScores.forEach(r -> playerIndividualTotal.put(r.getUniqueName(), new ArrayList<>()));

        for (List<RoundTotal> playerRoundTotal : playerRoundTotals.values()) {
            if (playerRoundTotal.isEmpty()) continue;

            String playerName = playerRoundTotal.getFirst().getUniqueName();
            int roundsToCount = getEventsToCount(playerRoundTotal.getFirst().getType());
            List<IndividualTotal> indTotal = new ArrayList<>();

            // Sort in descending order based on total score
            playerRoundTotal.sort(Comparator.comparingInt(RoundTotal::getTotal).reversed());

            Set<Integer> locationIds = new HashSet<>();
            for (RoundTotal t : playerRoundTotal) {
                if (indTotal.size() < roundsToCount - 1) {
                    // Add round directly until we reach the number of rounds to count
                    indTotal.add(toIndividualTotal(t));
                    locationIds.add(t.getLocationId());
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
        return new IndividualTotal(t.getLocationId(), t.getTeam(), t.getAthlete(), t.getClassification(), t.getGender(), t.getTotal(), t.getType());
    }

    // Method to determine if a round should be added based on location constraints
    private boolean shouldAddRound( RoundTotal t, Set<Integer> locationIds) {
        int locationId = t.getLocationId();
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
                int totalScore = totals.stream().mapToInt(IndividualTotal::getTotal).sum();

                playerFinalTotals.put(key, new IndividualTotal(
                        0,
                        firstTotal.getTeam(),
                        firstTotal.getAthlete(),
                        firstTotal.getClassification(),
                        firstTotal.getGender(),
                        totalScore,
                        firstTotal.getType()
                ));
            }
        });

        return playerFinalTotals;
    }

    public boolean singleRound(String roundType) {
        Set<String> validRounds = Set.of("clays", "doubles", "doublesskeet", "fivestand");
        return validRounds.contains(roundType);
    }

    private static Map<String, Integer> determineEventsToCount() {
        var roundCounts = new HashMap<String, Integer>();
        roundCounts.put("singles", 4);
        roundCounts.put("doubles", 4);
        roundCounts.put("handicap", 4);
        roundCounts.put("skeet", 4);
        roundCounts.put("clays", 3);
        roundCounts.put("fivestand", 4);
        roundCounts.put("doublesskeet", 4);
        return roundCounts;
    }

    public static int getEventsToCount(String type) {
        return roundCounts.getOrDefault(type, 0); // Default to 0 if type not found
    }
}
