package trap.report

import trap.model.IndividualTotal
import trap.model.RoundScore
import trap.model.RoundTotal
import java.util.function.Consumer

class TrapHelper {
    fun calculatePlayerRoundTotals(roundScores: List<RoundScore>): Map<String, ArrayList<RoundTotal>> {
        val playerRoundTotals: MutableMap<String, ArrayList<RoundTotal>> = HashMap()
        for (r in roundScores) {
            playerRoundTotals[r.uniqueName] = ArrayList()
        }
        for (r in roundScores) {
            val currentPlayerRoundTotal = playerRoundTotals[r.uniqueName]!!
            if (singleRound(r.type)) {
                currentPlayerRoundTotal.add(RoundTotal(r.eventId, r.locationId, r.team, r.athlete, r.classification, r.gender, r.round1, r.type))
                if (r.round2 > 0) {
                    currentPlayerRoundTotal.add(RoundTotal(r.eventId, r.locationId, r.team, r.athlete, r.classification, r.gender, r.round2, r.type))
                }
            } else {
                currentPlayerRoundTotal.add(RoundTotal(r.eventId, r.locationId, r.team, r.athlete, r.classification, r.gender, r.round1 + r.round2, r.type))
                if (r.round3 + r.round4 > 0) {
                    currentPlayerRoundTotal.add(RoundTotal(r.eventId, r.locationId, r.team, r.athlete, r.classification, r.gender, r.round3 + r.round4, r.type))
                    if (r.round5 + r.round6 > 0) {
                        currentPlayerRoundTotal.add(RoundTotal(r.eventId, r.locationId, r.team, r.athlete, r.classification, r.gender, r.round5 + r.round6, r.type))
                        if (r.round7 + r.round8 > 0) {
                            currentPlayerRoundTotal.add(RoundTotal(r.eventId, r.locationId, r.team, r.athlete, r.classification, r.gender, r.round7 + r.round8, r.type))
                        }
                    }
                }
            }
            playerRoundTotals[r.uniqueName] = currentPlayerRoundTotal
        }
        return playerRoundTotals
    }

    fun calculatePlayerIndividualTotal(roundScores: List<RoundScore>, playerRoundTotals: Map<String?, ArrayList<RoundTotal>>): Map<String, ArrayList<IndividualTotal>> {
        val playerIndividualTotal: MutableMap<String, ArrayList<IndividualTotal>> = HashMap()
        for (r in roundScores) {
            playerIndividualTotal[r.uniqueName] = ArrayList()
        }
        for (playerRoundTotal in playerRoundTotals.values) {
            // clays, skeet, and fivestand are top 3 scores only, minimum 2 locations
            val subtractScores = subtractScores(playerRoundTotal[0].type)
            val indTotal = ArrayList<IndividualTotal>()
            playerRoundTotal.sortWith(Comparator.comparingInt(RoundTotal::total).reversed())
            for (t in playerRoundTotal) {
                if (indTotal.size < 3 - subtractScores) {
                    indTotal.add(IndividualTotal(t.locationId, t.team!!, t.athlete!!, t.classification!!, t.gender!!, t.total, t.type!!))
                } else {
                    val locationIds: MutableSet<Int> = HashSet()
                    indTotal.forEach(Consumer { l: IndividualTotal -> locationIds.add(l.locationId) })
                    // if location doesn't already exist
                    if (!locationIds.contains(t.locationId)) {
                        indTotal.add(IndividualTotal(t.locationId, t.team!!, t.athlete!!, t.classification!!, t.gender!!, t.total, t.type!!))
                        break
                    }
                    // if location isn't the same as the other 3
                    if (locationIds.size == 2 || locationIds.size == 3) {
                        indTotal.add(IndividualTotal(t.locationId, t.team!!, t.athlete!!, t.classification!!, t.gender!!, t.total, t.type!!))
                        break
                    }
                }
            }
            playerIndividualTotal[playerRoundTotal[0].uniqueName] = indTotal
        }
        return playerIndividualTotal
    }

    fun calculatePlayerFinalTotal(playerIndividualTotal: Map<String, ArrayList<IndividualTotal>>): Map<String, IndividualTotal> {
        val playerFinalTotal: MutableMap<String, IndividualTotal> = HashMap()
        for ((key, value) in playerIndividualTotal) {
            if (value.isNotEmpty()) {
                var total = 0
                for (t in value) {
                    total += t.total
                }
                playerFinalTotal[key] = IndividualTotal(0, value[0].team, value[0].athlete, value[0].classification, value[0].gender, total, value[0].type)
            }
        }
        return playerFinalTotal
    }

    private fun subtractScores(roundType: String?): Int {
        return if (roundType == "clays" || roundType == "skeet" || roundType == "fivestand" || roundType == "doublesskeet") 1 else 0
    }

    private fun singleRound(roundType: String): Boolean {
        return roundType == "clays" || roundType == "doubles" || roundType == "doublesskeet"
    }
}
