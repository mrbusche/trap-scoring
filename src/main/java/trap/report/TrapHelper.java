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

    public Map<String, ArrayList<RoundTotal>> calculatePlayerRoundTotals(List<RoundScore> roundScores) {
        var playerRoundTotals = new HashMap<String, ArrayList<RoundTotal>>();
        for (var r : roundScores) {
            playerRoundTotals.put(r.getUniqueName(), new ArrayList<>());
        }
        for (var r : roundScores) {
            var currentPlayerRoundTotal = playerRoundTotals.get(r.getUniqueName());
            if (singleRound(r.getType())) {
                currentPlayerRoundTotal.add(new RoundTotal(r.getEventId(), r.getLocationId(), r.getTeam(), r.getAthlete(), r.getClassification(), r.getGender(), r.getRound1(), r.getType()));
                if (r.getRound2() > 0) {
                    currentPlayerRoundTotal.add(new RoundTotal(r.getEventId(), r.getLocationId(), r.getTeam(), r.getAthlete(), r.getClassification(), r.getGender(), r.getRound2(), r.getType()));
                }
            } else {
                currentPlayerRoundTotal.add(new RoundTotal(r.getEventId(), r.getLocationId(), r.getTeam(), r.getAthlete(), r.getClassification(), r.getGender(), r.getRound1() + r.getRound2(), r.getType()));
                if (r.getRound3() + r.getRound4() > 0) {
                    currentPlayerRoundTotal.add(new RoundTotal(r.getEventId(), r.getLocationId(), r.getTeam(), r.getAthlete(), r.getClassification(), r.getGender(), r.getRound3() + r.getRound4(), r.getType()));
                    if (r.getRound5() + r.getRound6() > 0) {
                        currentPlayerRoundTotal.add(new RoundTotal(r.getEventId(), r.getLocationId(), r.getTeam(), r.getAthlete(), r.getClassification(), r.getGender(), r.getRound5() + r.getRound6(), r.getType()));
                        if (r.getRound7() + r.getRound8() > 0) {
                            currentPlayerRoundTotal.add(new RoundTotal(r.getEventId(), r.getLocationId(), r.getTeam(), r.getAthlete(), r.getClassification(), r.getGender(), r.getRound7() + r.getRound8(), r.getType()));
                        }
                    }
                }
            }
            playerRoundTotals.put(r.getUniqueName(), currentPlayerRoundTotal);
        }

        return playerRoundTotals;
    }

    public Map<String, ArrayList<IndividualTotal>> calculatePlayerIndividualTotal(List<RoundScore> roundScores, Map<String, ArrayList<RoundTotal>> playerRoundTotals) {
        var playerIndividualTotal = new HashMap<String, ArrayList<IndividualTotal>>();
        for (var r : roundScores) {
            playerIndividualTotal.put(r.getUniqueName(), new ArrayList<>());
        }
        for (var playerRoundTotal : playerRoundTotals.values()) {
            var roundsToCount = getEventsToCount(playerRoundTotal.getFirst().getType());
            var indTotal = new ArrayList<IndividualTotal>();
            playerRoundTotal.sort(Comparator.comparingInt(RoundTotal::getTotal).reversed());
            for (var t : playerRoundTotal) {
                if (indTotal.size() < (roundsToCount - 1)) {
                    indTotal.add(new IndividualTotal(t.getLocationId(), t.getTeam(), t.getAthlete(), t.getClassification(), t.getGender(), t.getTotal(), t.getType()));
                } else {
                    var locationIds = new HashSet<Integer>();
                    indTotal.forEach(l -> locationIds.add(l.getLocationId()));
                    // if location doesn't already exist
                    if (!locationIds.contains(t.getLocationId())) {
                        indTotal.add(new IndividualTotal(t.getLocationId(), t.getTeam(), t.getAthlete(), t.getClassification(), t.getGender(), t.getTotal(), t.getType()));
                        break;
                    }
                    // if location isn't the same as the other 3
                    if (locationIds.size() == 2 || locationIds.size() == 3) {
                        indTotal.add(new IndividualTotal(t.getLocationId(), t.getTeam(), t.getAthlete(), t.getClassification(), t.getGender(), t.getTotal(), t.getType()));
                        break;
                    }
                }
            }
            playerIndividualTotal.put(playerRoundTotal.getFirst().getUniqueName(), indTotal);
        }

        return playerIndividualTotal;
    }

    public Map<String, IndividualTotal> calculatePlayerFinalTotal(Map<String, ArrayList<IndividualTotal>> playerIndividualTotals) {
        var playerFinalTotals = new HashMap<String, IndividualTotal>();
        playerIndividualTotals.forEach((key, totals) -> {
            if (!totals.isEmpty()) {
                var firstTotal = totals.get(0);
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
