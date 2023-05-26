package trap.model

class RoundTotal(val eventId: Int,
                 val locationId: Int,
                 val team: String? = null,
                 val athlete: String? = null,
                 val classification: String? = null,
                 val gender: String? = null,
                 val total: Int,
                 val type: String? = null) {

    val uniqueName: String
        get() = "$athlete $team $classification $type"
}
