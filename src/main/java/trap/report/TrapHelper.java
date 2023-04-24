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

    public HashMap<String, ArrayList<RoundTotal>> calculatePlayerRoundTotals(List<RoundScore> roundScores) {
        HashMap<String, ArrayList<RoundTotal>> playerRoundTotals = new HashMap<>();
        ArrayList<RoundTotal> playerRoundTotalList = new ArrayList<>();
        for (RoundScore r : roundScores) {
            playerRoundTotals.put(r.getUniqueName(), new ArrayList<>());
        }
        for (RoundScore r : roundScores) {
            var currentPlayerRoundTotal = playerRoundTotals.get(r.getUniqueName());
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
            playerRoundTotals.put(r.getUniqueName(), currentPlayerRoundTotal);
        }
        return playerRoundTotals;
    }

    public HashMap<String, ArrayList<IndividualTotal>> calculatePlayerIndividualTotal(List<RoundScore> roundScores, HashMap<String, ArrayList<RoundTotal>> playerRoundTotals) {
        HashMap<String, ArrayList<IndividualTotal>> playerIndividualTotal = new HashMap<>();
        for (RoundScore r : roundScores) {
            playerIndividualTotal.put(r.getUniqueName(), new ArrayList<>());
        }
        ArrayList<IndividualTotal> allIndTotals = new ArrayList<>();
        for (ArrayList<RoundTotal> playerRoundTotal : playerRoundTotals.values()) {
            ArrayList<IndividualTotal> indTotal = new ArrayList<>();
            playerRoundTotal.sort(Comparator.comparingInt(RoundTotal::getTotal).reversed());
            for(RoundTotal t: playerRoundTotal) {
                if (indTotal.size() < 3) {
                    indTotal.add(new IndividualTotal(t.getLocationId(), t.getTeam(), t.getAthlete(), t.getClassification(), t.getGender(), t.getTotal(), t.getType()));
                } else {
                    Set<Integer> locationIds = new HashSet<>();
                    indTotal.forEach(l -> locationIds.add(l.getLocationId()));
                    // if location doesn't already exist
                    if (!locationIds.contains(t.getLocationId())) {
                        indTotal.add(new IndividualTotal(t.getLocationId(), t.getTeam(), t.getAthlete(), t.getClassification(), t.getGender(), t.getTotal(), t.getType()));
                        break;
                    }
                    // if location isn't the same as the other 3
                    if (locationIds.size() == 1 && indTotal.get(0).getLocationId() != t.getLocationId()){
                        indTotal.add(new IndividualTotal(t.getLocationId(), t.getTeam(), t.getAthlete(), t.getClassification(), t.getGender(), t.getTotal(), t.getType()));
                        break;
                    }

                }
            }
            playerIndividualTotal.put(playerRoundTotal.get(0).getUniqueName(), indTotal);
        }
        return playerIndividualTotal;
    }

    public HashMap<String, IndividualTotal> calculatePlayerFinalTotal(HashMap<String, ArrayList<IndividualTotal>> playerIndividualTotal) {
        HashMap<String, IndividualTotal> playerFinalTotal = new HashMap<>();
        try {
            for (Map.Entry<String, ArrayList<IndividualTotal>> entry : playerIndividualTotal.entrySet()) {
                String key = entry.getKey();
                ArrayList<IndividualTotal> value = entry.getValue();
                if (value.size() != 0) {
                    int total = 0;
                    for (IndividualTotal t : value) {
                        total += t.getTotal();
                    }
                    playerFinalTotal.put(key, new IndividualTotal(0, value.get(0).getTeam(), value.get(0).getAthlete(), value.get(0).getClassification(), value.get(0).getGender(), total, value.get(0).getType()));
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return playerFinalTotal;
    }
}
