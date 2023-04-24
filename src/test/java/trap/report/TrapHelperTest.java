package trap.report;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import trap.model.RoundScore;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class TrapHelperTest {
    TrapHelper trapHelper = new TrapHelper();

    private List<RoundScore> getRoundScoresPlayer1() {
        List<RoundScore> roundScores = new ArrayList();

        roundScores.add(new RoundScore(16923, "NS Trap Fall Invitational", 1132, "Clinton County Sportsman Club", "2022-10-29", "Wilton Varsity", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 22, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(16945, "Stockdale/Jags season Opener! Sunday", 73, "Stockdale Gun Club", "2022-09-04", "UNI Trap Team", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 23, 24, 24, 24, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(16946, "Stockdale/Jags season Opener! Monday", 73, "Stockdale Gun Club", "2022-09-05", "UNI Trap Team", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 25, 21, 22, 24, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(16948, "Stockdale/Jags season Opener! Saturday", 73, "Stockdale Gun Club", "2022-09-03", "UNI Trap Team", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 25, 23, 23, 22, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17032, "Otter Creek September ATA Meet", 90, "Otter Creek Sportsmans Club", "2022-09-11", "UNI Trap Team", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 22, 22, 24, 20, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17177, "Maquoketa @ Potosi", 968, "Southwest Wisconsin Sportsmens Club", "2022-09-24", "Wilton 1", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17258, "Otter Creek October ATA Shoot", 90, "Otter Creek Sportsmans Club", "2022-10-09", "UNI Trap Team", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 24, 22, 24, 23, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17277, "Wilton Invitational", 48, "Muscatine Izaak Walton League", "2022-10-08", "Wilton / Mt. Pleasant - High School", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 23, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17321, "Wilton Invitational", 48, "Muscatine Izaak Walton League", "2022-10-22", "Wilton V", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17975, "Kennedy Trap Club Invitational", 90, "Otter Creek Sportsmans Club", "2023-04-15", "Wilton HS 1", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(18256, "Wilton and Solon", 48, "Muscatine Izaak Walton League", "2023-04-08", "Wilton Varsity 1", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 22, 24, 0, 0, 0, 0, 0, 0, "singles"));

        return roundScores;
    }

    private List<RoundScore> getRoundScoresPlayer2() {
        List<RoundScore> roundScores = new ArrayList();

        roundScores.add(new RoundScore(17258, "Otter Creek October ATA Shoot", 1, "Otter Creek Sportsmans Club", "2022-10-09", "UNI Trap Team", "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 25, 24, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17277, "Wilton Invitational", 1, "Muscatine Izaak Walton League", "2022-10-08", "Wilton / Mt. Pleasant - High School", "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17321, "Wilton Invitational", 1, "Muscatine Izaak Walton League", "2022-10-22", "Wilton V", "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17975, "Kennedy Trap Club Invitational", 1, "Otter Creek Sportsmans Club", "2023-04-15", "Wilton HS 1", "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(18256, "Wilton and Solon", 2, "Muscatine Izaak Walton League", "2023-04-08", "Wilton Varsity 1", "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 20, 20, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(18256, "Wilton and Solon", 2, "Muscatine Izaak Walton League", "2023-04-08", "Wilton Varsity 1", "UNI Trap Team", "Matt Busche", "Intermediate/Advanced", "M", 20, 7, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(18256, "Wilton and Solon", 2, "Muscatine Izaak Walton League", "2023-04-08", "Wilton Varsity 1", "UNI Trap Team", "Matt Busche", "Intermediate/Advanced", "M", 20, 20, 0, 0, 0, 0, 0, 0, "doubles"));

        return roundScores;
    }

    @Test
    void testPlayerRoundTotals() {
        var roundScoresPlayer1 = getRoundScoresPlayer1();
        var roundScoresPlayer2 = getRoundScoresPlayer2();
        List<RoundScore> roundScores = new ArrayList<>();
        roundScores.addAll(roundScoresPlayer1);
        roundScores.addAll(roundScoresPlayer2);
        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);

        var player1 = playerRoundTotals.get(roundScoresPlayer1.get(0).getUniqueName());
        assertEquals(16, player1.size());
        var player2 = playerRoundTotals.get(roundScoresPlayer2.get(0).getUniqueName());
        assertEquals(6, player2.size());
    }

    @Test
    void testPlayerIndividualTotals() {
        var roundScoresPlayer1 = getRoundScoresPlayer1();
        var roundScoresPlayer2 = getRoundScoresPlayer2();
        List<RoundScore> roundScores = new ArrayList<>();
        roundScores.addAll(roundScoresPlayer1);
        roundScores.addAll(roundScoresPlayer2);
        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);

        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);

        var player1 = playerIndividualTotal.get(roundScoresPlayer1.get(0).getUniqueName());
        assertEquals(4, player1.size());
        assertEquals(49, player1.get(0).getTotal());
        assertEquals(48, player1.get(1).getTotal());
        assertEquals(48, player1.get(2).getTotal());
        assertEquals(48, player1.get(3).getTotal());

        var player2 = playerIndividualTotal.get(roundScoresPlayer2.get(0).getUniqueName());
        assertEquals(4, player2.size());
        assertEquals(49, player2.get(0).getTotal());
        assertEquals(49, player2.get(1).getTotal());
        assertEquals(49, player2.get(2).getTotal());
        assertEquals(40, player2.get(3).getTotal());
    }

    @Test
    void testPlayerFinalTotal() {
        var roundScoresPlayer1 = getRoundScoresPlayer1();
        var roundScoresPlayer2 = getRoundScoresPlayer2();
        List<RoundScore> roundScores = new ArrayList<>();
//        roundScores.addAll(roundScoresPlayer1);
        roundScores.addAll(roundScoresPlayer2);
        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

//        var player1 = roundScoresPlayer1.get(0);
//        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
//        assertEquals("UNI Trap Team", playerFinalTotal.get(player1.getUniqueName()).getTeam());
//        assertEquals("Scott W Busche", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
//        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
//        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
//        assertEquals(193, playerFinalTotal.get(player1.getUniqueName()).getTotal());
//        assertEquals("singles", playerFinalTotal.get(player1.getUniqueName()).getType());

        var player2 = roundScoresPlayer2.get(0);

        assertEquals(3, playerFinalTotal.size());

        var firstResult = roundScoresPlayer2.get(0).getUniqueName();
        assertEquals(0, playerFinalTotal.get(firstResult).getLocationId());
        assertEquals("UNI Trap Team", playerFinalTotal.get(firstResult).getTeam());
        assertEquals("Matt Busche", playerFinalTotal.get(firstResult).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(firstResult).getClassification());
        assertEquals("M", playerFinalTotal.get(firstResult).getGender());
        assertEquals(187, playerFinalTotal.get(firstResult).getTotal());
        assertEquals("singles", playerFinalTotal.get(firstResult).getType());

        var secondResult = roundScoresPlayer2.get(5).getUniqueName();
        assertEquals(0, playerFinalTotal.get(secondResult).getLocationId());
        assertEquals("UNI Trap Team", playerFinalTotal.get(secondResult).getTeam());
        assertEquals("Matt Busche", playerFinalTotal.get(secondResult).getAthlete());
        assertEquals("Intermediate/Advanced", playerFinalTotal.get(secondResult).getClassification());
        assertEquals("M", playerFinalTotal.get(secondResult).getGender());
        assertEquals(27, playerFinalTotal.get(secondResult).getTotal());
        assertEquals("singles", playerFinalTotal.get(secondResult).getType());

        var thirdResult = roundScoresPlayer2.get(6).getUniqueName();
        assertEquals(0, playerFinalTotal.get(thirdResult).getLocationId());
        assertEquals("UNI Trap Team", playerFinalTotal.get(thirdResult).getTeam());
        assertEquals("Matt Busche", playerFinalTotal.get(thirdResult).getAthlete());
        assertEquals("Intermediate/Advanced", playerFinalTotal.get(thirdResult).getClassification());
        assertEquals("M", playerFinalTotal.get(thirdResult).getGender());
        assertEquals(40, playerFinalTotal.get(thirdResult).getTotal());
        assertEquals("doubles", playerFinalTotal.get(thirdResult).getType());
    }
}
