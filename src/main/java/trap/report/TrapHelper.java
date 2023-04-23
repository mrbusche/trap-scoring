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

    public HashMap<String, ArrayList<IndividualTotal>> calculatePlayerIndividualTotal(List<RoundScore> roundScores, HashMap<String, ArrayList<RoundTotal>> playerRoundTotals) {
        HashMap<String, ArrayList<IndividualTotal>> playerIndividualTotal = new HashMap<>();
        for (RoundScore r : roundScores) {
            playerIndividualTotal.put(r.getAthlete() + " " + r.getTeam(), new ArrayList<>());
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
                        playerIndividualTotal.put(t.getAthlete() + " " + t.getTeam(), indTotal);
                        break;
                    }
                    // if location isn't the same as the other 3
                    if (locationIds.size() == 1 && indTotal.get(0).getLocationId() != t.getLocationId()){
                        indTotal.add(new IndividualTotal(t.getLocationId(), t.getTeam(), t.getAthlete(), t.getClassification(), t.getGender(), t.getTotal(), t.getType()));
                        playerIndividualTotal.put(t.getAthlete() + " " + t.getTeam(), indTotal);
                        break;
                    }

                }
            }
            System.out.println(playerIndividualTotal);
        }
        return playerIndividualTotal;
    }

    public HashMap<String, ArrayList<RoundTotal>> calculatePlayerRoundTotals(List<RoundScore> roundScores) {

        HashMap<String, ArrayList<RoundTotal>> playerRoundTotals = new HashMap<>();
        ArrayList<RoundTotal> playerRoundTotalList = new ArrayList<>();
        for (RoundScore r : roundScores) {
            playerRoundTotals.put(r.getAthlete() + " " + r.getTeam(), new ArrayList<>());
        }
        for (RoundScore r : roundScores) {
            var currentPlayerRoundTotal = playerRoundTotals.get(r.getAthlete() + " " + r.getTeam());
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
            playerRoundTotals.put(r.getAthlete() + " " + r.getTeam(), currentPlayerRoundTotal);
        }
        return playerRoundTotals;
    }


    public HashMap<String, IndividualTotal> calculatePlayerFinalTotal(HashMap<String, ArrayList<IndividualTotal>> playerIndividualTotal) {
        HashMap<String, IndividualTotal> playerFinalTotal = new HashMap<>();
        for (Map.Entry<String, ArrayList<IndividualTotal>> entry : playerIndividualTotal.entrySet()) {
            String key = entry.getKey();
            ArrayList<IndividualTotal> value = entry.getValue();
            int total = 0;
            for (IndividualTotal t : value) {
                total += t.getTotal();
            }
            playerFinalTotal.put(key, new IndividualTotal(0, value.get(0).getTeam(), value.get(0).getAthlete(), value.get(0).getClassification(), value.get(0).getGender(), total, value.get(0).getType()));
        }
        return playerFinalTotal;
    }
}
