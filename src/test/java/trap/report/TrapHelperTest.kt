package trap.report

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import trap.model.RoundScore
import trap.model.RoundTotal

@SpringBootTest
internal class TrapHelperTest {
    var trapHelper = TrapHelper()
    private val roundScoresPlayer1: List<RoundScore>
        get() {
            val roundScores: MutableList<RoundScore> = ArrayList()
            roundScores.add(RoundScore(16923, "NS Trap Fall Invitational", 1132, "Clinton County Sportsman Club", "2022-10-29", "Wilton Varsity", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 22, 23, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(16945, "Stockdale/Jags season Opener! Sunday", 73, "Stockdale Gun Club", "2022-09-04", "UNI Trap Team", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 23, 24, 24, 24, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(16946, "Stockdale/Jags season Opener! Monday", 73, "Stockdale Gun Club", "2022-09-05", "UNI Trap Team", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 25, 21, 22, 24, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(16948, "Stockdale/Jags season Opener! Saturday", 73, "Stockdale Gun Club", "2022-09-03", "UNI Trap Team", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 25, 23, 23, 22, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17032, "Otter Creek September ATA Meet", 90, "Otter Creek Sportsmans Club", "2022-09-11", "UNI Trap Team", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 22, 22, 24, 20, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17177, "Maquoketa @ Potosi", 968, "Southwest Wisconsin Sportsmens Club", "2022-09-24", "Wilton 1", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17258, "Otter Creek October ATA Shoot", 90, "Otter Creek Sportsmans Club", "2022-10-09", "UNI Trap Team", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 24, 22, 24, 23, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17277, "Wilton Invitational", 48, "Muscatine Izaak Walton League", "2022-10-08", "Wilton / Mt. Pleasant - High School", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 23, 23, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17321, "Wilton Invitational", 48, "Muscatine Izaak Walton League", "2022-10-22", "Wilton V", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17975, "Kennedy Trap Club Invitational", 90, "Otter Creek Sportsmans Club", "2023-04-15", "Wilton HS 1", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 24, 24, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(18256, "Wilton and Solon", 48, "Muscatine Izaak Walton League", "2023-04-08", "Wilton Varsity 1", "UNI Trap Team", "Scott W Busche", "Senior/Varsity", "M", 22, 24, 0, 0, 0, 0, 0, 0, "singles"))
            return roundScores
        }
    private val roundScoresPlayer2: List<RoundScore>
        get() {
            val roundScores: MutableList<RoundScore> = ArrayList()
            roundScores.add(RoundScore(17258, "Otter Creek October ATA Shoot", 1, "Otter Creek Sportsmans Club", "2022-10-09", "UNI Trap Team", "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 25, 24, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17277, "Wilton Invitational", 1, "Muscatine Izaak Walton League", "2022-10-08", "Wilton / Mt. Pleasant - High School", "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17321, "Wilton Invitational", 1, "Muscatine Izaak Walton League", "2022-10-22", "Wilton V", "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17975, "Kennedy Trap Club Invitational", 1, "Otter Creek Sportsmans Club", "2023-04-15", "Wilton HS 1", "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(18256, "Wilton and Solon", 2, "Muscatine Izaak Walton League", "2023-04-08", "Wilton Varsity 1", "UNI Trap Team", "Matt Busche", "Senior/Varsity", "M", 20, 20, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(18256, "Wilton and Solon", 2, "Muscatine Izaak Walton League", "2023-04-08", "Wilton Varsity 1", "UNI Trap Team", "Matt Busche", "Intermediate/Advanced", "M", 20, 7, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(18256, "Wilton and Solon", 2, "Muscatine Izaak Walton League", "2023-04-08", "Wilton Varsity 1", "UNI Trap Team", "Matt Busche", "Intermediate/Advanced", "M", 20, 20, 0, 0, 0, 0, 0, 0, "doubles"))
            return roundScores
        }
    private val roundScoresPlayer3: List<RoundScore>
        get() {
            val roundScores: MutableList<RoundScore> = ArrayList()
            roundScores.add(RoundScore(16892, "Pleasant Valley Invitational Trap Meet - 1st 50 Singles", 1132, "Clinton County Sportsman Club", "2022-09-24", "DYT HS 1 Singles 1st 50", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 24, 23, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(16920, "NORTH SCOTT TRAP FALL INVITATIONAL FIRST 50 SINGLES", 1132, "Clinton County Sportsman Club", "2022-09-17", "DYT HS Singles 1 1st 50", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 25, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(16945, "Stockdale/Jags season Opener! Sunday", 73, "Stockdale Gun Club", "2022-09-04", "DYT 1", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 24, 25, 25, 25, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(16946, "Stockdale/Jags season Opener! Monday", 73, "Stockdale Gun Club", "2022-09-05", "DYT1", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 25, 25, 25, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(16948, "Stockdale/Jags season Opener! Saturday", 73, "Stockdale Gun Club", "2022-09-03", "Davenport 1", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 25, 25, 25, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17163, "NORTH SCOTT FALL INVITATIONAL SECOND 50 SINGLES", 1132, "Clinton County Sportsman Club", "2022-09-17", "DYT HS Singles 1 2nd 50", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17267, "Davenport Youth Trap Fall Invite 1st 50", 1132, "Clinton County Sportsman Club", "2022-10-15", "DYT Mix 1 1st 50", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 23, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17751, "Singles Spring Shoot", 1132, "Clinton County Sportsman Club", "2023-04-08", "DYT SENIOR SINGLES 3", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 24, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(18511, "Bettendorf Bulldog Trap (CCSC ATA)", 1132, "Clinton County Sportsman Club", "2023-04-23", "Davenport trap", "Minnesota Vikings", "Justin Jefferson", "Senior/Varsity", "M", 25, 25, 25, 24, 0, 0, 0, 0, "singles"))
            return roundScores
        }
    private val roundScoresPlayer4: List<RoundScore>
        get() {
            val roundScores: MutableList<RoundScore> = ArrayList()
            roundScores.add(RoundScore(16933, "September 3/4 NSCA/SCTP Sporting Clays 1st 100 rounds", 986, "Black Oak Clays", "2022-09-03", "Ankeny Jags", "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 48, 48, 0, 0, 0, 0, 0, 0, "clays"))
            roundScores.add(RoundScore(16940, "NPGC NSCA SPORTING CLAYS", 50, "New Pioneer Clay Target Center", "2022-09-11", "Ankeny Centennial Jaguars Shooting Sports", "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 45, 44, 0, 0, 0, 0, 0, 0, "clays"))
            roundScores.add(RoundScore(16941, "NPGC NSCA SPORTING CLAYS (TRUE PAIR EVENT)", 50, "New Pioneer Clay Target Center", "2022-10-09", "Ankeny Centennial Jaguars Shooting Sports 2", "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 44, 40, 0, 0, 0, 0, 0, 0, "clays"))
            roundScores.add(RoundScore(17159, "Sporting Clays 9/24-9/25 NSCA/SCTP First 100", 986, "Black Oak Clays", "2022-09-24", "Ankeny Jags mIx", "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 43, 46, 0, 0, 0, 0, 0, 0, "clays"))
            roundScores.add(RoundScore(17987, "Spring Begins shoot 4/1-4/2", 1070, "Anita Shooting Complex", "2023-04-01", "Jags", "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 41, 41, 0, 0, 0, 0, 0, 0, "clays"))
            roundScores.add(RoundScore(18168, "April Sporting", 42, "Mahaska County Ikes", "2023-04-09", "Ankeny Centennial Jaguars Shooting Sports 2", "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 42, 45, 0, 0, 0, 0, 0, 0, "clays"))
            roundScores.add(RoundScore(18220, "NPGC March Monthly", 50, "New Pioneer Clay Target Center", "2023-03-26", "Ankeny Centennial Jaguars Shooting Sports", "Ankeny Centennial Jaguars Shooting Sports", "Sam LaPorta", "Senior/Varsity", "M", 38, 43, 0, 0, 0, 0, 0, 0, "clays"))
            return roundScores
        }
    private val roundScoresPlayer5: List<RoundScore>
        get() {
            val roundScores: MutableList<RoundScore> = ArrayList()
            roundScores.add(RoundScore(16923, "NS Trap Fall Invitational", 1132, "Clinton County Sportsman Club", "2022-10-29", "Wilton JV/IA", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 21, 21, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17166, "Camanche Trap", 1132, "Clinton County Sportsman Club", "2022-10-01", "Wilton - Intermediates", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 22, 22, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17267, "Davenport Youth Trap Fall Invite 1st 50", 1132, "Clinton County Sportsman Club", "2022-10-15", "Wilton Intermediates", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 22, 24, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17268, "Davenport Youth Trap Fall Invite 2nd 50", 1132, "Clinton County Sportsman Club", "2022-10-15", "Wilton Intermediates", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 18, 24, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17277, "Wilton Invitational", 48, "Muscatine Izaak Walton League", "2022-10-08", "Wilton / Danville - Intermediates", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 20, 22, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17321, "Wilton Invitational", 48, "Muscatine Izaak Walton League", "2022-10-22", "Wilton IA", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 23, 25, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17813, "Maquoketa @ Dubuque- Open", 22, "Dubuque Co Izaak Walton League", "2023-04-01", "Wilton Int Singles", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 18, 12, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(17975, "Kennedy Trap Club Invitational", 90, "Otter Creek Sportsmans Club", "2023-04-15", "Wilton Int 2", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 23, 23, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(18128, "Wilton and North Scott", 48, "Muscatine Izaak Walton League", "2023-04-22", "Wilton IA 1", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 21, 22, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(18256, "Wilton and Solon", 48, "Muscatine Izaak Walton League", "2023-04-08", "New London / Wilton", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 21, 25, 0, 0, 0, 0, 0, 0, "singles"))
            roundScores.add(RoundScore(18499, "Wilton vs. Highland", 48, "Muscatine Izaak Walton League", "2023-04-23", "Wilton IA", "Wilton Trap Team", "TJ Hockenson", "Intermediate/Advanced", "M", 24, 22, 0, 0, 0, 0, 0, 0, "singles"))
            return roundScores
        }

    @Test
    fun testPlayerRoundTotals() {
        val roundScoresPlayer1 = roundScoresPlayer1
        val roundScoresPlayer2 = roundScoresPlayer2
        val roundScores: MutableList<RoundScore> = ArrayList()
        roundScores.addAll(roundScoresPlayer1)
        roundScores.addAll(roundScoresPlayer2)
        val playerRoundTotals = trapHelper.calculatePlayerRoundTotals(roundScores)
        val player1 = playerRoundTotals[roundScoresPlayer1[0].uniqueName]!!
        Assertions.assertEquals(16, player1.size)
        val player2 = playerRoundTotals[roundScoresPlayer2[0].uniqueName]!!
        Assertions.assertEquals(6, player2.size)
    }

    @Test
    fun testPlayerIndividualTotals() {
        val roundScoresPlayer1 = roundScoresPlayer1
        val roundScoresPlayer2 = roundScoresPlayer2
        val roundScores: MutableList<RoundScore> = ArrayList()
        roundScores.addAll(roundScoresPlayer1)
        roundScores.addAll(roundScoresPlayer2)
        val playerRoundTotals: Map<String, ArrayList<RoundTotal>> = trapHelper.calculatePlayerRoundTotals(roundScores)
        val playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals)
        val player1 = playerIndividualTotal[roundScoresPlayer1[0].uniqueName]!!
        Assertions.assertEquals(4, player1.size)
        Assertions.assertEquals(49, player1[0].total)
        Assertions.assertEquals(48, player1[1].total)
        Assertions.assertEquals(48, player1[2].total)
        Assertions.assertEquals(48, player1[3].total)
        val player2 = playerIndividualTotal[roundScoresPlayer2[0].uniqueName]!!
        Assertions.assertEquals(4, player2.size)
        Assertions.assertEquals(49, player2[0].total)
        Assertions.assertEquals(49, player2[1].total)
        Assertions.assertEquals(49, player2[2].total)
        Assertions.assertEquals(40, player2[3].total)
    }

    @Test
    fun testPlayerFinalTotal() {
        val roundScoresPlayer1 = roundScoresPlayer1
        val roundScoresPlayer2 = roundScoresPlayer2
        val roundScores: MutableList<RoundScore> = ArrayList()
        roundScores.addAll(roundScoresPlayer1)
        roundScores.addAll(roundScoresPlayer2)
        val playerRoundTotals: Map<String, ArrayList<RoundTotal>> = trapHelper.calculatePlayerRoundTotals(roundScores)
        val playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals)
        val playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal)
        val player1 = roundScoresPlayer1[0]
        Assertions.assertEquals(0, playerFinalTotal[player1.uniqueName]!!.locationId)
        Assertions.assertEquals("UNI Trap Team", playerFinalTotal[player1.uniqueName]!!.team)
        Assertions.assertEquals("Scott W Busche", playerFinalTotal[player1.uniqueName]!!.athlete)
        Assertions.assertEquals("Senior/Varsity", playerFinalTotal[player1.uniqueName]!!.classification)
        Assertions.assertEquals("M", playerFinalTotal[player1.uniqueName]!!.gender)
        Assertions.assertEquals(193, playerFinalTotal[player1.uniqueName]!!.total)
        Assertions.assertEquals("singles", playerFinalTotal[player1.uniqueName]!!.type)
        val player2 = roundScoresPlayer2[0]
        Assertions.assertEquals(4, playerFinalTotal.size)
        val firstResult = roundScoresPlayer2[0].uniqueName
        Assertions.assertEquals(0, playerFinalTotal[firstResult]!!.locationId)
        Assertions.assertEquals("UNI Trap Team", playerFinalTotal[firstResult]!!.team)
        Assertions.assertEquals("Matt Busche", playerFinalTotal[firstResult]!!.athlete)
        Assertions.assertEquals("Senior/Varsity", playerFinalTotal[firstResult]!!.classification)
        Assertions.assertEquals("M", playerFinalTotal[firstResult]!!.gender)
        Assertions.assertEquals(187, playerFinalTotal[firstResult]!!.total)
        Assertions.assertEquals("singles", playerFinalTotal[firstResult]!!.type)
        val secondResult = roundScoresPlayer2[5].uniqueName
        Assertions.assertEquals(0, playerFinalTotal[secondResult]!!.locationId)
        Assertions.assertEquals("UNI Trap Team", playerFinalTotal[secondResult]!!.team)
        Assertions.assertEquals("Matt Busche", playerFinalTotal[secondResult]!!.athlete)
        Assertions.assertEquals("Intermediate/Advanced", playerFinalTotal[secondResult]!!.classification)
        Assertions.assertEquals("M", playerFinalTotal[secondResult]!!.gender)
        Assertions.assertEquals(27, playerFinalTotal[secondResult]!!.total)
        Assertions.assertEquals("singles", playerFinalTotal[secondResult]!!.type)
        val thirdResult = roundScoresPlayer2[6].uniqueName
        Assertions.assertEquals(0, playerFinalTotal[thirdResult]!!.locationId)
        Assertions.assertEquals("UNI Trap Team", playerFinalTotal[thirdResult]!!.team)
        Assertions.assertEquals("Matt Busche", playerFinalTotal[thirdResult]!!.athlete)
        Assertions.assertEquals("Intermediate/Advanced", playerFinalTotal[thirdResult]!!.classification)
        Assertions.assertEquals("M", playerFinalTotal[thirdResult]!!.gender)
        Assertions.assertEquals(40, playerFinalTotal[thirdResult]!!.total)
        Assertions.assertEquals("doubles", playerFinalTotal[thirdResult]!!.type)
    }

    @Test
    fun testPlayerFinalTotal2() {
        val roundScores = roundScoresPlayer3
        val playerRoundTotals: Map<String, ArrayList<RoundTotal>> = trapHelper.calculatePlayerRoundTotals(roundScores)
        val playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals)
        val playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal)
        val player1 = roundScores[0]
        Assertions.assertEquals(0, playerFinalTotal[player1.uniqueName]!!.locationId)
        Assertions.assertEquals("Minnesota Vikings", playerFinalTotal[player1.uniqueName]!!.team)
        Assertions.assertEquals("Justin Jefferson", playerFinalTotal[player1.uniqueName]!!.athlete)
        Assertions.assertEquals("Senior/Varsity", playerFinalTotal[player1.uniqueName]!!.classification)
        Assertions.assertEquals("M", playerFinalTotal[player1.uniqueName]!!.gender)
        Assertions.assertEquals(200, playerFinalTotal[player1.uniqueName]!!.total)
        Assertions.assertEquals("singles", playerFinalTotal[player1.uniqueName]!!.type)
    }

    @Test
    fun testClaysScoring() {
        val roundScores = roundScoresPlayer4
        val playerRoundTotals: Map<String, ArrayList<RoundTotal>> = trapHelper.calculatePlayerRoundTotals(roundScores)
        val playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals)
        val playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal)
        val player1 = roundScores[0]
        Assertions.assertEquals(0, playerFinalTotal[player1.uniqueName]!!.locationId)
        Assertions.assertEquals("Ankeny Centennial Jaguars Shooting Sports", playerFinalTotal[player1.uniqueName]!!.team)
        Assertions.assertEquals("Sam LaPorta", playerFinalTotal[player1.uniqueName]!!.athlete)
        Assertions.assertEquals("Senior/Varsity", playerFinalTotal[player1.uniqueName]!!.classification)
        Assertions.assertEquals("M", playerFinalTotal[player1.uniqueName]!!.gender)
        Assertions.assertEquals(141, playerFinalTotal[player1.uniqueName]!!.total)
        Assertions.assertEquals("clays", playerFinalTotal[player1.uniqueName]!!.type)
    }

    @Test
    fun testPlayerFinalTotal5() {
        val roundScores = roundScoresPlayer5
        val playerRoundTotals: Map<String, ArrayList<RoundTotal>> = trapHelper.calculatePlayerRoundTotals(roundScores)
        val playerIndividualTotal = trapHelper.calculatePlayerIndividualTotal(roundScores, playerRoundTotals)
        val playerFinalTotal = trapHelper.calculatePlayerFinalTotal(playerIndividualTotal)
        val player1 = roundScores[0]
        Assertions.assertEquals(0, playerFinalTotal[player1.uniqueName]!!.locationId)
        Assertions.assertEquals("Wilton Trap Team", playerFinalTotal[player1.uniqueName]!!.team)
        Assertions.assertEquals("TJ Hockenson", playerFinalTotal[player1.uniqueName]!!.athlete)
        Assertions.assertEquals("Intermediate/Advanced", playerFinalTotal[player1.uniqueName]!!.classification)
        Assertions.assertEquals("M", playerFinalTotal[player1.uniqueName]!!.gender)
        Assertions.assertEquals(186, playerFinalTotal[player1.uniqueName]!!.total)
        Assertions.assertEquals("singles", playerFinalTotal[player1.uniqueName]!!.type)
    }
}
