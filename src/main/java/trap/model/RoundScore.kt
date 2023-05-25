package trap.model

class RoundScore(val eventId: Int,
                 val event: String?,
                 val locationId: Int,
                 val location: String?,
                 val eventDate: String?,
                 val squadName: String?,
                 val team: String?,
                 val athlete: String?,
                 val classification: String?,
                 val gender: String?,
                 val round1: Int,
                 val round2: Int,
                 val round3: Int,
                 val round4: Int,
                 val round5: Int,
                 val round6: Int,
                 val round7: Int,
                 val round8: Int,
                 val type: String) {
    val uniqueName: String
        get() = "$athlete $team $classification $type"
}
