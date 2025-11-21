package trap.report;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import trap.model.TrapRoundScore;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TrapServiceTest {
    final TrapService trapService = new TrapService();

    private List<TrapRoundScore> getRoundScoresPlayer1() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16923, 1132, "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 22, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(16945, 73, "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 23, 24, 24, 24, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(16946, 73, "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 25, 21, 22, 24, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(16948, 73, "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 25, 23, 23, 22, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17032, 90, "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 22, 22, 24, 20, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17177, 968, "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17258, 90, "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 24, 22, 24, 23, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17277, 48, "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 23, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17321, 48, "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17975, 90, "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(18256, 48, "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 22, 24, 0, 0, 0, 0, 0, 0, "singles"));

        return roundScores;
    }

    private List<TrapRoundScore> getRoundScoresPlayer2() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(17258, 1, "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 25, 24, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17277, 1, "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17321, 1, "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17975, 1, "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(18256, 2, "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 20, 20, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(18256, 2, "UNI Trap Team", "Matt Busche", "Intermediate/Advanced", "M", 20, 7, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(18256, 2, "UNI Trap Team", "Matt Busche", "Intermediate/Advanced", "M", 20, 20, 0, 0, 0, 0, 0, 0, "doubles"));

        return roundScores;
    }

    private List<TrapRoundScore> getRoundScoresPlayer3() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16892, 1132, "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 24, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(16920, 1132, "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 25, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(16945, 73, "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 24, 25, 25, 25, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(16946, 73, "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 25, 25, 25, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(16948, 73, "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 25, 25, 25, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17163, 1132, "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17267, 1132, "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17751, 1132, "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(18511, 1132, "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 25, 25, 24, 0, 0, 0, 0, "singles"));

        return roundScores;
    }

    private List<TrapRoundScore> getRoundScoresPlayer4() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 48, 48, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new TrapRoundScore(16940, 50, "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 45, 44, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new TrapRoundScore(16941, 50, "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 44, 40, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new TrapRoundScore(17159, 986, "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 43, 46, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new TrapRoundScore(17987, 1070, "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 41, 41, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new TrapRoundScore(18168, 42, "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 42, 45, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new TrapRoundScore(18220, 50, "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 38, 43, 0, 0, 0, 0, 0, 0, "clays"));

        return roundScores;
    }

    private List<TrapRoundScore> getRoundScoresPlayer5() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16923, 1132, "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 21, 21, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17166, 1132, "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 22, 22, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17267, 1132, "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 22, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17268, 1132, "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 18, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17277, 48, "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 20, 22, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17321, 48, "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 23, 25, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17813, 22, "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 18, 12, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17975, 90, "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 23, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(18128, 48, "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 21, 22, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(18256, 48, "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 21, 25, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(18499, 48, "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 24, 22, 0, 0, 0, 0, 0, 0, "singles"));

        return roundScores;
    }

    @Test
    void playerRoundTotals() {
        var roundScoresPlayer1 = getRoundScoresPlayer1();
        var roundScoresPlayer2 = getRoundScoresPlayer2();
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.addAll(roundScoresPlayer1);
        roundScores.addAll(roundScoresPlayer2);
        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);

        var player1 = playerRoundTotals.get(roundScoresPlayer1.getFirst().uniqueName());
        var player2 = playerRoundTotals.get(roundScoresPlayer2.getFirst().uniqueName());

        assertAll(
            () -> assertEquals(16, player1.size()),
            () -> assertEquals(6, player2.size())
        );
    }

    @Test
    void playerIndividualTotals() {
        var roundScoresPlayer1 = getRoundScoresPlayer1();
        var roundScoresPlayer2 = getRoundScoresPlayer2();
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.addAll(roundScoresPlayer1);
        roundScores.addAll(roundScoresPlayer2);
        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);

        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);

        var player1 = playerIndividualTotal.get(roundScoresPlayer1.getFirst().uniqueName());
        var player2 = playerIndividualTotal.get(roundScoresPlayer2.getFirst().uniqueName());

        assertAll(
            () -> assertEquals(5, player1.size()),
            () -> assertEquals(49, player1.getFirst().total()),
            () -> assertEquals(48, player1.get(1).total()),
            () -> assertEquals(48, player1.get(2).total()),
            () -> assertEquals(48, player1.get(3).total()),
            () -> assertEquals(48, player1.get(4).total()),
            () -> assertEquals(4, player2.size()), // Only 2 locations, max 4 scores
            () -> assertEquals(49, player2.getFirst().total()),
            () -> assertEquals(49, player2.get(1).total()),
            () -> assertEquals(49, player2.get(2).total()),
            () -> assertEquals(40, player2.get(3).total())
        );
    }

    @Test
    void playerFinalTotal() {
        var roundScoresPlayer1 = getRoundScoresPlayer1();
        var roundScoresPlayer2 = getRoundScoresPlayer2();

        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.addAll(roundScoresPlayer1);
        roundScores.addAll(roundScoresPlayer2);
        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScoresPlayer1.getFirst();
        var firstResult = roundScoresPlayer2.getFirst().uniqueName();
        var secondResult = roundScoresPlayer2.get(5).uniqueName();
        var thirdResult = roundScoresPlayer2.get(6).uniqueName();

        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("UNI Trap Team", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Scott W Busche", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(241, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("singles", playerFinalTotal.get(player1.uniqueName()).type()),
            () -> assertEquals(4, playerFinalTotal.size()),
            () -> assertEquals(0, playerFinalTotal.get(firstResult).locationId()),
            () -> assertEquals("UNI Trap Team", playerFinalTotal.get(firstResult).team()),
            () -> assertEquals("Matt Busche", playerFinalTotal.get(firstResult).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(firstResult).classification()),
            () -> assertEquals("M", playerFinalTotal.get(firstResult).gender()),
            () -> assertEquals(187, playerFinalTotal.get(firstResult).total()),
            () -> assertEquals("singles", playerFinalTotal.get(firstResult).type()),
            () -> assertEquals(0, playerFinalTotal.get(secondResult).locationId()),
            () -> assertEquals("UNI Trap Team", playerFinalTotal.get(secondResult).team()),
            () -> assertEquals("Matt Busche", playerFinalTotal.get(secondResult).athlete()),
            () -> assertEquals("Intermediate/Advanced", playerFinalTotal.get(secondResult).classification()),
            () -> assertEquals("M", playerFinalTotal.get(secondResult).gender()),
            () -> assertEquals(27, playerFinalTotal.get(secondResult).total()),
            () -> assertEquals("singles", playerFinalTotal.get(secondResult).type()),
            () -> assertEquals(0, playerFinalTotal.get(thirdResult).locationId()),
            () -> assertEquals("UNI Trap Team", playerFinalTotal.get(thirdResult).team()),
            () -> assertEquals("Matt Busche", playerFinalTotal.get(thirdResult).athlete()),
            () -> assertEquals("Intermediate/Advanced", playerFinalTotal.get(thirdResult).classification()),
            () -> assertEquals("M", playerFinalTotal.get(thirdResult).gender()),
            () -> assertEquals(40, playerFinalTotal.get(thirdResult).total()),
            () -> assertEquals("doubles", playerFinalTotal.get(thirdResult).type())
        );
    }

    @Test
    void playerFinalTotal2With2Locations() {
        var roundScores = getRoundScoresPlayer3();
        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Minnesota Vikings", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Justin Jefferson", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(200, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("singles", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void claysScoring() {
        var roundScores = getRoundScoresPlayer4();
        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Ankeny Centennial Jaguars Shooting Sports", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(186, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("clays", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void playerFinalTotal5() {
        var roundScores = getRoundScoresPlayer5();
        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Wilton Trap Team", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("TJ Hockenson", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Intermediate/Advanced", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(232, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("singles", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void singlesScoring4Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17159, 983, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(18168, 21, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "singles"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(181, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("singles", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void singlesScoring3Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17159, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(18168, 21, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "singles"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(181, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("singles", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void singlesScoring2Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 24, 24, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(17159, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(18168, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 21, 0, 0, 0, 0, 0, 0, "singles"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(185, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("singles", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void singlesScoring2LocationsFavorOneLocation() {
        var roundScores = new ArrayList<TrapRoundScore>();
        // Location 1 stronger
        roundScores.add(new TrapRoundScore(20001, 201, "Team Y", "Player B", "Senior/Varsity", "M", 25, 25, 0, 0, 0, 0, 0, 0, "singles")); // 50
        roundScores.add(new TrapRoundScore(20002, 201, "Team Y", "Player B", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles")); // 49
        roundScores.add(new TrapRoundScore(20003, 201, "Team Y", "Player B", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles")); // 48
        // Location 2
        roundScores.add(new TrapRoundScore(20004, 202, "Team Y", "Player B", "Senior/Varsity", "M", 24, 23, 0, 0, 0, 0, 0, 0, "singles")); // 47
        roundScores.add(new TrapRoundScore(20005, 202, "Team Y", "Player B", "Senior/Varsity", "M", 23, 23, 0, 0, 0, 0, 0, 0, "singles")); // 46

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var key = roundScores.getFirst().uniqueName();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(key).locationId()),
            () -> assertEquals("Team Y", playerFinalTotal.get(key).team()),
            () -> assertEquals("Player B", playerFinalTotal.get(key).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(key).classification()),
            () -> assertEquals("M", playerFinalTotal.get(key).gender()),
            () -> assertEquals(194, playerFinalTotal.get(key).total()), // 50 + 49 + 48 + 47 (2 locations => 5 - 1 = 4 scores)
            () -> assertEquals("singles", playerFinalTotal.get(key).type())
        );
    }

    @Test
    void singlesScoring2LocationsWithHigherScoresInOneLocation() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(16943, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(16953, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(16963, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(16973, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "singles"));
        roundScores.add(new TrapRoundScore(18168, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "singles"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(189, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("singles", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void doublesScoring4Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 45, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new TrapRoundScore(17159, 983, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 44, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new TrapRoundScore(18168, 21, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doubles"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(205, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("doubles", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void singlesScoring3LocationsFavorOneLocation() {
        var roundScores = new ArrayList<TrapRoundScore>();
        // Location 1 dominates
        roundScores.add(new TrapRoundScore(30001, 301, "Team Z", "Player C", "Senior/Varsity", "M", 25, 25, 0, 0, 0, 0, 0, 0, "singles")); // 50
        roundScores.add(new TrapRoundScore(30002, 301, "Team Z", "Player C", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles")); // 49
        // Location 2
        roundScores.add(new TrapRoundScore(30003, 302, "Team Z", "Player C", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles")); // 48
        // Location 3
        roundScores.add(new TrapRoundScore(30004, 303, "Team Z", "Player C", "Senior/Varsity", "M", 24, 23, 0, 0, 0, 0, 0, 0, "singles")); // 47
        roundScores.add(new TrapRoundScore(30005, 303, "Team Z", "Player C", "Senior/Varsity", "M", 23, 23, 0, 0, 0, 0, 0, 0, "singles")); // 46

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var key = roundScores.getFirst().uniqueName();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(key).locationId()),
            () -> assertEquals("Team Z", playerFinalTotal.get(key).team()),
            () -> assertEquals("Player C", playerFinalTotal.get(key).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(key).classification()),
            () -> assertEquals("M", playerFinalTotal.get(key).gender()),
            () -> assertEquals(240, playerFinalTotal.get(key).total()),
            () -> assertEquals("singles", playerFinalTotal.get(key).type())
        );
    }

    @Test
    void doublesScoring3Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 22, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new TrapRoundScore(17159, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new TrapRoundScore(18168, 21, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doubles"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(205, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("doubles", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void singlesScoring4LocationsOneLocationHasMultipleHigher() {
        var roundScores = new ArrayList<TrapRoundScore>();
        // Location 1 strongest with multiple high scores
        roundScores.add(new TrapRoundScore(40001, 401, "Team A1", "Player D", "Senior/Varsity", "M", 25, 25, 0, 0, 0, 0, 0, 0, "singles")); // 50
        roundScores.add(new TrapRoundScore(40002, 401, "Team A1", "Player D", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles")); // 49
        roundScores.add(new TrapRoundScore(40003, 401, "Team A1", "Player D", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles")); // 48
        // Other locations
        roundScores.add(new TrapRoundScore(40004, 402, "Team A1", "Player D", "Senior/Varsity", "M", 24, 23, 0, 0, 0, 0, 0, 0, "singles")); // 47
        roundScores.add(new TrapRoundScore(40005, 403, "Team A1", "Player D", "Senior/Varsity", "M", 23, 23, 0, 0, 0, 0, 0, 0, "singles")); // 46
        roundScores.add(new TrapRoundScore(40006, 404, "Team A1", "Player D", "Senior/Varsity", "M", 22, 23, 0, 0, 0, 0, 0, 0, "singles")); // 45

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var key = roundScores.getFirst().uniqueName();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(key).locationId()),
            () -> assertEquals("Team A1", playerFinalTotal.get(key).team()),
            () -> assertEquals("Player D", playerFinalTotal.get(key).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(key).classification()),
            () -> assertEquals("M", playerFinalTotal.get(key).gender()),
            () -> assertEquals(240, playerFinalTotal.get(key).total()),
            () -> assertEquals("singles", playerFinalTotal.get(key).type())
        );
    }

    @Test
    void doublesScoring2Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 48, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 45, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new TrapRoundScore(17159, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 44, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new TrapRoundScore(18168, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 42, 0, 0, 0, 0, 0, 0, "doubles"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(185, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("doubles", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void doublesScoring1LocationTop3OnlySingleRoundHandling() {
        var roundScores = new ArrayList<TrapRoundScore>();
        // doubles is singleRound => each round counts separately if > 0
        roundScores.add(new TrapRoundScore(50001, 501, "Team D1", "Player E", "Senior/Varsity", "M", 48, 47, 0, 0, 0, 0, 0, 0, "doubles")); // 48, 47
        roundScores.add(new TrapRoundScore(50002, 501, "Team D1", "Player E", "Senior/Varsity", "M", 46, 45, 0, 0, 0, 0, 0, 0, "doubles")); // 46, 45

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var key = roundScores.getFirst().uniqueName();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(key).locationId()),
            () -> assertEquals("Team D1", playerFinalTotal.get(key).team()),
            () -> assertEquals("Player E", playerFinalTotal.get(key).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(key).classification()),
            () -> assertEquals("M", playerFinalTotal.get(key).gender()),
            () -> assertEquals(141, playerFinalTotal.get(key).total()),
            () -> assertEquals("doubles", playerFinalTotal.get(key).type())
        );
    }

    @Test
    void doublesScoring2LocationsWithHigherScoresInOneLocation() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 48, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new TrapRoundScore(16943, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new TrapRoundScore(16953, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new TrapRoundScore(16963, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 48, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new TrapRoundScore(16973, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 22, 0, 0, 0, 0, 0, 0, "doubles"));
        roundScores.add(new TrapRoundScore(18168, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doubles"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(189, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("doubles", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void handicapScoring4Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new TrapRoundScore(17159, 983, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new TrapRoundScore(18168, 21, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "handicap"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(181, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("handicap", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void handicapScoring3Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new TrapRoundScore(17159, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new TrapRoundScore(18168, 21, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "handicap"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(181, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("handicap", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void handicapScoring2Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 20, 20, 24, 24, 0, 0, 0, 0, "handicap"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new TrapRoundScore(17159, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new TrapRoundScore(18168, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 21, 0, 0, 0, 0, 0, 0, "handicap"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(179, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("handicap", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void handicapScoring2LocationsWithHigherScoresInOneLocation() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new TrapRoundScore(16943, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new TrapRoundScore(16953, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new TrapRoundScore(16963, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new TrapRoundScore(16973, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "handicap"));
        roundScores.add(new TrapRoundScore(18168, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "handicap"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(189, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("handicap", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void skeetScoring3Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new TrapRoundScore(17159, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new TrapRoundScore(17159, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "skeet"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(181, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("skeet", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void skeetScoring2Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 20, 20, 24, 24, 0, 0, 0, 0, "skeet"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new TrapRoundScore(17159, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new TrapRoundScore(18168, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 21, 0, 0, 0, 0, 0, 0, "skeet"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(179, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("skeet", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void skeetScoring2LocationsWithHigherScoresInOneLocation() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new TrapRoundScore(16943, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new TrapRoundScore(16953, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new TrapRoundScore(16963, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new TrapRoundScore(16973, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 23, 22, 0, 0, 0, 0, 0, 0, "skeet"));
        roundScores.add(new TrapRoundScore(18168, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 21, 23, 0, 0, 0, 0, 0, 0, "skeet"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(189, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("skeet", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void claysScoring3Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 96, 23, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 93, 22, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new TrapRoundScore(17159, 123, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 88, 23, 0, 0, 0, 0, 0, 0, "clays"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(300, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("clays", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void claysScoring3LocationsWithHigherScoresInOneLocation() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 96, 88, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new TrapRoundScore(16943, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 96, 88, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new TrapRoundScore(16953, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 96, 88, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new TrapRoundScore(16963, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 96, 88, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new TrapRoundScore(16973, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 96, 88, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 90, 22, 0, 0, 0, 0, 0, 0, "clays"));
        roundScores.add(new TrapRoundScore(18168, 12, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 88, 23, 0, 0, 0, 0, 0, 0, "clays"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(370, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("clays", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void doublesskeetScoring4Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 22, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new TrapRoundScore(17159, 123, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new TrapRoundScore(17159, 24, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(204, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("doublesskeet", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void doublesskeetScoring3Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 0, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 0, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new TrapRoundScore(17159, 123, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 0, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new TrapRoundScore(17169, 123, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(205, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("doublesskeet", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void doublesskeetScoring2Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new TrapRoundScore(16940, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 22, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new TrapRoundScore(17159, 123, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new TrapRoundScore(17159, 123, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(181, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("doublesskeet", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void doublesskeetScoring3LocationsWithHigherScoresInOneLocation() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 44, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new TrapRoundScore(16943, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 44, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new TrapRoundScore(16953, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 44, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new TrapRoundScore(16963, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 44, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new TrapRoundScore(16973, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 44, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 22, 0, 0, 0, 0, 0, 0, "doublesskeet"));
        roundScores.add(new TrapRoundScore(18168, 12, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "doublesskeet"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(233, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("doublesskeet", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void fivestandScoring3Locations() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 23, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 22, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new TrapRoundScore(17159, 123, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new TrapRoundScore(17159, 123, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 50, 23, 0, 0, 0, 0, 0, 0, "fivestand"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(210, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("fivestand", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void fivestandScoring3LocationsWithHigherScoresInOneLocation() {
        var roundScores = new ArrayList<TrapRoundScore>();
        roundScores.add(new TrapRoundScore(16933, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new TrapRoundScore(16943, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new TrapRoundScore(16953, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new TrapRoundScore(16963, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new TrapRoundScore(16973, 986, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 48, 24, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new TrapRoundScore(16940, 50, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 45, 22, 0, 0, 0, 0, 0, 0, "fivestand"));
        roundScores.add(new TrapRoundScore(18168, 12, "Team Name", "Sam LaPorta", "Senior/Varsity", "M", 44, 23, 0, 0, 0, 0, 0, 0, "fivestand"));

        var playerRoundTotals = trapService.calculatePlayerRoundTotals(roundScores);
        var playerIndividualTotal = trapService.calculatePlayerIndividualTotal(roundScores, playerRoundTotals);
        var playerFinalTotal = trapService.calculatePlayerFinalTotal(playerIndividualTotal);

        var player1 = roundScores.getFirst();
        assertAll(
            () -> assertEquals(0, playerFinalTotal.get(player1.uniqueName()).locationId()),
            () -> assertEquals("Team Name", playerFinalTotal.get(player1.uniqueName()).team()),
            () -> assertEquals("Sam LaPorta", playerFinalTotal.get(player1.uniqueName()).athlete()),
            () -> assertEquals("Senior/Varsity", playerFinalTotal.get(player1.uniqueName()).classification()),
            () -> assertEquals("M", playerFinalTotal.get(player1.uniqueName()).gender()),
            () -> assertEquals(233, playerFinalTotal.get(player1.uniqueName()).total()),
            () -> assertEquals("fivestand", playerFinalTotal.get(player1.uniqueName()).type())
        );
    }

    @Test
    void eventsToCount() {
        assertAll(
            () -> assertEquals(5, TrapService.getEventsToCount("singles")),
            () -> assertEquals(5, TrapService.getEventsToCount("doubles")),
            () -> assertEquals(5, TrapService.getEventsToCount("handicap")),
            () -> assertEquals(5, TrapService.getEventsToCount("skeet")),
            () -> assertEquals(5, TrapService.getEventsToCount("fivestand")),
            () -> assertEquals(5, TrapService.getEventsToCount("doublesskeet")),
            () -> assertEquals(4, TrapService.getEventsToCount("clays"))
        );
    }

    @Test
    void roundsToCount() {
        assertAll(
            () -> assertTrue(trapService.isSingleRound("clays")),
            () -> assertTrue(trapService.isSingleRound("doubles")),
            () -> assertTrue(trapService.isSingleRound("fivestand")),
            () -> assertTrue(trapService.isSingleRound("doublesskeet")),
            () -> assertFalse(trapService.isSingleRound("singles")),
            () -> assertFalse(trapService.isSingleRound("handicap")),
            () -> assertFalse(trapService.isSingleRound("skeet"))
        );
    }
}
