package trap.model

class IndividualTotal(val locationId: Int, val team: String, val athlete: String, val classification: String, val gender: String, val total: Int, val type: String) {

    val teamForScores: String
        get() = "$type $team $teamClassificationForTotal"
    val teamClassification: String
        get() = classification.replace("Senior/Varsity", "Varsity").replace("Senior/Jr. Varsity", "Junior Varsity").replace("Intermediate/Advanced", "Intermediate Advanced").replace("Intermediate/Entry Level", "Intermediate Entry")
    val teamClassificationForTotal: String
        get() = teamClassification.replace("Senior/Jr. Varsity", "Varsity").replace("Senior/Varsity", "Varsity").replace("Junior Varsity", "Varsity").replace("Intermediate Advanced", "Intermediate Entry")
}
