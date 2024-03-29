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

public class TrapHelper {

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
            // clays, skeet, and fivestand are top 3 scores only, minimum 2 locations
            var subtractScores = subtractScores(playerRoundTotal.getFirst().getType());
            var indTotal = new ArrayList<IndividualTotal>();
            playerRoundTotal.sort(Comparator.comparingInt(RoundTotal::getTotal).reversed());
            for (var t : playerRoundTotal) {
                if (indTotal.size() < (3 - subtractScores)) {
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

    public Map<String, IndividualTotal> calculatePlayerFinalTotal(Map<String, ArrayList<IndividualTotal>> playerIndividualTotal) {
        var playerFinalTotal = new HashMap<String, IndividualTotal>();
        for (Map.Entry<String, ArrayList<IndividualTotal>> entry : playerIndividualTotal.entrySet()) {
            var key = entry.getKey();
            ArrayList<IndividualTotal> value = entry.getValue();
            if (!value.isEmpty()) {
                int total = 0;
                for (var t : value) {
                    total += t.getTotal();
                }
                playerFinalTotal.put(key, new IndividualTotal(0, value.getFirst().getTeam(), value.getFirst().getAthlete(), value.getFirst().getClassification(), value.getFirst().getGender(), total, value.getFirst().getType()));
            }
        }

        return playerFinalTotal;
    }

    private int subtractScores(String roundType) {
        return roundType.equals("clays") || roundType.equals("skeet") || roundType.equals("fivestand") || roundType.equals("doublesskeet") ? 1 : 0;
    }

    private boolean singleRound(String roundType) {
        return roundType.equals("clays") || roundType.equals("doubles") || roundType.equals("doublesskeet");
    }
}
