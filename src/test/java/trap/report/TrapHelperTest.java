package trap.report;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import trap.model.RoundScore;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TrapHelperTest {
    TrapHelper trapHelper = new TrapHelper();

    private List<RoundScore> getRoundScoresPlayer1() {
        var roundScores = new ArrayList<RoundScore>();

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
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(17258, "Otter Creek October ATA Shoot", 1, "Otter Creek Sportsmans Club", "2022-10-09", "UNI Trap Team", "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 25, 24, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17277, "Wilton Invitational", 1, "Muscatine Izaak Walton League", "2022-10-08", "Wilton / Mt. Pleasant - High School", "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17321, "Wilton Invitational", 1, "Muscatine Izaak Walton League", "2022-10-22", "Wilton V", "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17975, "Kennedy Trap Club Invitational", 1, "Otter Creek Sportsmans Club", "2023-04-15", "Wilton HS 1", "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(18256, "Wilton and Solon", 2, "Muscatine Izaak Walton League", "2023-04-08", "Wilton Varsity 1", "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 20, 20, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(18256, "Wilton and Solon", 2, "Muscatine Izaak Walton League", "2023-04-08", "Wilton Varsity 1", "UNI Trap Team", "Matt Busche", "Intermediate/Advanced", "M", 20, 7, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(18256, "Wilton and Solon", 2, "Muscatine Izaak Walton League", "2023-04-08", "Wilton Varsity 1", "UNI Trap Team", "Matt Busche", "Intermediate/Advanced", "M", 20, 20, 0, 0, 0, 0, 0, 0, "doubles"));

        return roundScores;
    }

    private List<RoundScore> getRoundScoresPlayer3() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16892, "Pleasant Valley Invitational Trap Meet - 1st 50 Singles", 1132, "Clinton County Sportsman Club", "2022-09-24", "DYT HS 1 Singles 1st 50", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 24, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(16920, "NORTH SCOTT TRAP FALL INVITATIONAL FIRST 50 SINGLES", 1132, "Clinton County Sportsman Club", "2022-09-17", "DYT HS Singles 1 1st 50", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 25, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(16945, "Stockdale/Jags season Opener! Sunday", 73, "Stockdale Gun Club", "2022-09-04", "DYT 1", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 24, 25, 25, 25, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(16946, "Stockdale/Jags season Opener! Monday", 73, "Stockdale Gun Club", "2022-09-05", "DYT1", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 25, 25, 25, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(16948, "Stockdale/Jags season Opener! Saturday", 73, "Stockdale Gun Club", "2022-09-03", "Davenport 1", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 25, 25, 25, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17163, "NORTH SCOTT FALL INVITATIONAL SECOND 50 SINGLES", 1132, "Clinton County Sportsman Club", "2022-09-17", "DYT HS Singles 1 2nd 50", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17267, "Davenport Youth Trap Fall Invite 1st 50", 1132, "Clinton County Sportsman Club", "2022-10-15", "DYT Mix 1 1st 50", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17751, "Singles Spring Shoot", 1132, "Clinton County Sportsman Club", "2023-04-08", "DYT SENIOR SINGLES 3", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(18511, "Bettendorf Bulldog Trap (CCSC ATA)", 1132, "Clinton County Sportsman Club", "2023-04-23", "Davenport trap", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 25, 25, 24, 0, 0, 0, 0, "singles"));

        return roundScores;
    }

    private List<RoundScore> getRoundScoresPlayer4() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "September 3/4 NSCA/SCTP Sporting Clays 1st 100 rounds", 986, "Black Oak Clays", "2022-09-03", "Ankeny Jags", "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 48, 48, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new RoundScore(16940, "NPGC NSCA SPORTING CLAYS", 50, "New Pioneer Clay Target Center", "2022-09-11", "Ankeny Centennial Jaguars Shooting Sports", "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 45, 44, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new RoundScore(16941, "NPGC NSCA SPORTING CLAYS (TRUE PAIR EVENT)", 50, "New Pioneer Clay Target Center", "2022-10-09", "Ankeny Centennial Jaguars Shooting Sports 2", "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 44, 40, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new RoundScore(17159, "Sporting Clays 9/24-9/25 NSCA/SCTP First 100", 986, "Black Oak Clays", "2022-09-24", "Ankeny Jags mIx", "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 43, 46, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new RoundScore(17987, "Spring Begins shoot 4/1-4/2", 1070, "Anita Shooting Complex", "2023-04-01", "Jags", "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 41, 41, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new RoundScore(18168, "April Sporting", 42, "Mahaska County Ikes", "2023-04-09", "Ankeny Centennial Jaguars Shooting Sports 2", "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 42, 45, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new RoundScore(18220, "NPGC March Monthly", 50, "New Pioneer Clay Target Center", "2023-03-26", "Ankeny Centennial Jaguars Shooting Sports", "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 38, 43, 0, 0, 0, 0, 0, 0, "clays"));

        return roundScores;
    }

    private List<RoundScore> getRoundScoresPlayer5() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16923, "NS Trap Fall Invitational", 1132, "Clinton County Sportsman Club", "2022-10-29", "Wilton JV/IA", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 21, 21, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17166, "Camanche Trap", 1132, "Clinton County Sportsman Club", "2022-10-01", "Wilton - Intermediates", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 22, 22, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17267, "Davenport Youth Trap Fall Invite 1st 50", 1132, "Clinton County Sportsman Club", "2022-10-15", "Wilton Intermediates", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 22, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17268, "Davenport Youth Trap Fall Invite 2nd 50", 1132, "Clinton County Sportsman Club", "2022-10-15", "Wilton Intermediates", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 18, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17277, "Wilton Invitational", 48, "Muscatine Izaak Walton League", "2022-10-08", "Wilton / Danville - Intermediates", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 20, 22, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17321, "Wilton Invitational", 48, "Muscatine Izaak Walton League", "2022-10-22", "Wilton IA", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 23, 25, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17813, "Maquoketa @ Dubuque- Open", 22, "Dubuque Co Izaak Walton League", "2023-04-01", "Wilton Int Singles", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 18, 12, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17975, "Kennedy Trap Club Invitational", 90, "Otter Creek Sportsmans Club", "2023-04-15", "Wilton Int 2", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 23, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(18128, "Wilton and North Scott", 48, "Muscatine Izaak Walton League", "2023-04-22", "Wilton IA 1", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 21, 22, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(18256, "Wilton and Solon", 48, "Muscatine Izaak Walton League", "2023-04-08", "New London / Wilton", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 21, 25, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(18499, "Wilton vs. Highland", 48, "Muscatine Izaak Walton League", "2023-04-23", "Wilton IA", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 24, 22, 0, 0, 0, 0, 0, 0, "singles"));

        return roundScores;
    }

    @Test
    void playerRoundTotals() {
        var roundScoresPlayer1 = getRoundScoresPlayer1();
        var roundScoresPlayer2 = getRoundScoresPlayer2();
        var roundScores = new ArrayList<RoundScore>();
        roundScores.addAll(roundScoresPlayer1);
        roundScores.addAll(roundScoresPlayer2);
        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);

        var player1 = playerRoundTotals.get(roundScoresPlayer1.getFirst().getUniqueName());
        assertEquals(16, player1.size());
        var player2 = playerRoundTotals.get(roundScoresPlayer2.getFirst().getUniqueName());
        assertEquals(6, player2.size());
    }

    @Test
    void playerIndividualTotals() {
        var roundScoresPlayer1 = getRoundScoresPlayer1();
        var roundScoresPlayer2 = getRoundScoresPlayer2();
        var roundScores = new ArrayList<RoundScore>();
        roundScores.addAll(roundScoresPlayer1);
        roundScores.addAll(roundScoresPlayer2);
        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);

        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);

        var player1 = playerIndividualTotal.get(roundScoresPlayer1.getFirst().getUniqueName());
        assertEquals(4, player1.size());
        assertEquals(49, player1.getFirst().getTotal());
        assertEquals(48, player1.get(1).getTotal());
        assertEquals(48, player1.get(2).getTotal());
        assertEquals(48, player1.get(3).getTotal());

        var player2 = playerIndividualTotal.get(roundScoresPlayer2.getFirst().getUniqueName());
        assertEquals(4, player2.size());
        assertEquals(49, player2.getFirst().getTotal());
        assertEquals(49, player2.get(1).getTotal());
        assertEquals(49, player2.get(2).getTotal());
        assertEquals(40, player2.get(3).getTotal());
    }

    @Test
    void playerFinalTotal() {
        var roundScoresPlayer1 = getRoundScoresPlayer1();
        var roundScoresPlayer2 = getRoundScoresPlayer2();
        var roundScores = new ArrayList<RoundScore>();
        roundScores.addAll(roundScoresPlayer1);
        roundScores.addAll(roundScoresPlayer2);
        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScoresPlayer1.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("UNI Trap Team", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Scott W Busche", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(193, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("singles", playerFinalTotal.get(player1.getUniqueName()).getType());

        assertEquals(4, playerFinalTotal.size());

        var firstResult = roundScoresPlayer2.getFirst().getUniqueName();
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

    @Test
    void playerFinalTotal2() {
        var roundScores = getRoundScoresPlayer3();
        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Minnesota Vikings", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Justin Jefferson", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(200, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("singles", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void claysScoring() {
        var roundScores = getRoundScoresPlayer4();
        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Ankeny Centennial Jaguars Shooting Sports", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(141, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("clays", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void playerFinalTotal5() {
        var roundScores = getRoundScoresPlayer5();
        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Wilton Trap Team", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("TJ Hockenson", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Intermediate/Advanced", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(186, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("singles", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void singlesScoring4Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(16940, "Event 2", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17159, "Event 4", 983, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(18168, "Event 6", 21, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "singles"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(181, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("singles", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void singlesScoring3Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(16940, "Event 2", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17159, "Event 4", 986, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(18168, "Event 6", 21, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "singles"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(181, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("singles", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void singlesScoring2Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 24, 24, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(16940, "Event 2", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(17159, "Event 4", 986, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(18168, "Event 6", 50, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 21, 0, 0, 0, 0, 0, 0, "singles"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(185, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("singles", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void singlesScoring2LocationsWithHigherScoresInOneLocation() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(16943, "Event 2", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(16953, "Event 3", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(16963, "Event 4", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(16973, "Event 5", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(16940, "Event 6", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new RoundScore(18168, "Event 7", 50, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "singles"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(189, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("singles", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void doublesScoring4Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new RoundScore(16940, "Event 2", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 45, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new RoundScore(17159, "Event 4", 983, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 44, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new RoundScore(18168, "Event 6", 21, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doubles"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(181, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("doubles", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void doublesScoring3Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new RoundScore(16940, "Event 2", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 22, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new RoundScore(17159, "Event 4", 986, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new RoundScore(18168, "Event 6", 21, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doubles"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(181, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("doubles", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void doublesScoring2Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 48, 48, 48, 0, 0, 0, 0, "doubles"));
        roundScores.add(new RoundScore(16940, "Event 2", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 45, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new RoundScore(17159, "Event 4", 986, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 44, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new RoundScore(18168, "Event 6", 50, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 42, 0, 0, 0, 0, 0, 0, "doubles"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(185, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("doubles", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void doublesScoring2LocationsWithHigherScoresInOneLocation() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 48, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new RoundScore(16943, "Event 2", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new RoundScore(16953, "Event 3", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new RoundScore(16963, "Event 4", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 48, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new RoundScore(16973, "Event 5", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new RoundScore(16940, "Event 6", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 22, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new RoundScore(18168, "Event 7", 50, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doubles"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(189, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("doubles", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void handicapScoring4Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new RoundScore(16940, "Event 2", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new RoundScore(17159, "Event 4", 983, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new RoundScore(18168, "Event 6", 21, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "handicap"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(181, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("handicap", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void handicapScoring3Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new RoundScore(16940, "Event 2", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new RoundScore(17159, "Event 4", 986, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new RoundScore(18168, "Event 6", 21, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "handicap"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(181, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("handicap", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void handicapScoring2Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 20, 20, 24, 24, 0, 0, 0, 0, "handicap"));
        roundScores.add(new RoundScore(16940, "Event 2", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new RoundScore(17159, "Event 4", 986, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new RoundScore(18168, "Event 6", 50, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 21, 0, 0, 0, 0, 0, 0, "handicap"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(179, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("handicap", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void handicapScoring2LocationsWithHigherScoresInOneLocation() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new RoundScore(16943, "Event 2", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new RoundScore(16953, "Event 3", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new RoundScore(16963, "Event 4", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new RoundScore(16973, "Event 5", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new RoundScore(16940, "Event 6", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new RoundScore(18168, "Event 7", 50, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "handicap"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(189, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("handicap", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void skeetScoring3Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new RoundScore(16940, "Event 2", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new RoundScore(17159, "Event 4", 986, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new RoundScore(17159, "Event 4", 986, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "skeet"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(181, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("skeet", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void skeetScoring2Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 20, 20, 24, 24, 0, 0, 0, 0, "skeet"));
        roundScores.add(new RoundScore(16940, "Event 2", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new RoundScore(17159, "Event 4", 986, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new RoundScore(18168, "Event 6", 50, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 21, 0, 0, 0, 0, 0, 0, "skeet"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(179, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("skeet", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void skeetScoring2LocationsWithHigherScoresInOneLocation() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new RoundScore(16943, "Event 2", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new RoundScore(16953, "Event 3", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new RoundScore(16963, "Event 4", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new RoundScore(16973, "Event 5", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new RoundScore(16940, "Event 6", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new RoundScore(18168, "Event 7", 50, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "skeet"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(189, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("skeet", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void claysScoring3Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 96, 23, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new RoundScore(16940, "Event 2", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 93, 22, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new RoundScore(17159, "Event 4", 123, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 88, 23, 0, 0, 0, 0, 0, 0, "clays"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(277, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("clays", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void claysScoring3LocationsWithHigherScoresInOneLocation() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 96, 88, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new RoundScore(16943, "Event 2", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 96, 88, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new RoundScore(16953, "Event 3", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 96, 88, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new RoundScore(16963, "Event 4", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 96, 88, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new RoundScore(16973, "Event 5", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 96, 88, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new RoundScore(16940, "Event 6", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 90, 22, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new RoundScore(18168, "Event 7", 12, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 88, 23, 0, 0, 0, 0, 0, 0, "clays"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(282, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("clays", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void doublesskeetScoring4Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new RoundScore(16940, "Event 2", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 22, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new RoundScore(17159, "Event 3", 123, "Location 3", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new RoundScore(17159, "Event 4", 24, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(181, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("doublesskeet", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void doublesskeetScoring3Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new RoundScore(16940, "Event 2", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 22, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new RoundScore(17159, "Event 3", 123, "Location 3", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new RoundScore(17159, "Event 4", 123, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(181, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("doublesskeet", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void doublesskeetScoring2Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new RoundScore(16940, "Event 2", 986, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 22, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new RoundScore(17159, "Event 3", 123, "Location 3", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new RoundScore(17159, "Event 4", 123, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(181, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("doublesskeet", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void doublesskeetScoring3LocationsWithHigherScoresInOneLocation() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 44, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new RoundScore(16943, "Event 2", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 44, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new RoundScore(16953, "Event 3", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 44, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new RoundScore(16963, "Event 4", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 44, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new RoundScore(16973, "Event 5", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 44, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new RoundScore(16940, "Event 6", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 22, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new RoundScore(18168, "Event 7", 12, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(189, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("doublesskeet", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void fivestandScoring3Locations() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 23, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new RoundScore(16940, "Event 2", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 22, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new RoundScore(17159, "Event 3", 123, "Location 3", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new RoundScore(17159, "Event 4", 123, "Location 4", "2022-09-24", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 50, 23, 0, 0, 0, 0, 0, 0, "fivestand"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(187, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("fivestand", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void fivestandScoring3LocationsWithHigherScoresInOneLocation() {
        var roundScores = new ArrayList<RoundScore>();

        roundScores.add(new RoundScore(16933, "Event 1", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new RoundScore(16943, "Event 2", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new RoundScore(16953, "Event 3", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new RoundScore(16963, "Event 4", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new RoundScore(16973, "Event 5", 986, "Location 1", "2022-09-03", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new RoundScore(16940, "Event 6", 50, "Location 2", "2022-09-11", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 22, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new RoundScore(18168, "Event 7", 12, "Location 6", "2023-04-09", "Squad Name", "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "fivestand"));

        var playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertEquals(0, playerFinalTotal.get(player1.getUniqueName()).getLocationId());
        assertEquals("Team Name", playerFinalTotal.get(player1.getUniqueName()).getTeam());
        assertEquals("Sam LaPorta", playerFinalTotal.get(player1.getUniqueName()).getAthlete());
        assertEquals("Senior/Varsity", playerFinalTotal.get(player1.getUniqueName()).getClassification());
        assertEquals("M", playerFinalTotal.get(player1.getUniqueName()).getGender());
        assertEquals(189, playerFinalTotal.get(player1.getUniqueName()).getTotal());
        assertEquals("fivestand", playerFinalTotal.get(player1.getUniqueName()).getType());
    }

    @Test
    void eventsToCount() {
        assertEquals(4, TrapHelper.getEventsToCount("singles"));
        assertEquals(4, TrapHelper.getEventsToCount("doubles"));
        assertEquals(4, TrapHelper.getEventsToCount("handicap"));
        assertEquals(4, TrapHelper.getEventsToCount("skeet"));
        assertEquals(4, TrapHelper.getEventsToCount("fivestand"));
        assertEquals(4, TrapHelper.getEventsToCount("doublesskeet"));

        assertEquals(3, TrapHelper.getEventsToCount("clays"));
    }

    @Test
    void roundsToCount() {
        assertTrue(trapHelper.singleRound("clays"));
        assertTrue(trapHelper.singleRound("doubles"));
        assertTrue(trapHelper.singleRound("fivestand"));
        assertTrue(trapHelper.singleRound("doublesskeet"));

        assertFalse(trapHelper.singleRound("singles"));
        assertFalse(trapHelper.singleRound("handicap"));
        assertFalse(trapHelper.singleRound("skeet"));

    }
}
