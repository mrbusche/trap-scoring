package trap.model;

public record IndividualTotal(
        int locationId,
        String team,
        String athlete,
        String classification,
        String gender,
        int total,
        String type
) {

    public String teamForScores() {
        return type + " " + team + " " + teamClassificationForTotal();
    }

    public String teamClassification() {
        return switch (classification) {
            case "Senior/Varsity" -> "Varsity";
            case "Senior/Jr. Varsity", "Junior Varsity" -> "Junior Varsity";
            case "Intermediate/Advanced" -> "Intermediate Advanced";
            case "Intermediate/Entry Level" -> "Intermediate Entry";
            default -> classification;
        };
    }

    public String teamClassificationForTotal() {
        return switch (teamClassification()) {
            case "Senior/Varsity", "Senior/Jr. Varsity", "Varsity", "Junior Varsity" -> "Varsity";
            case "Intermediate Advanced" -> "Intermediate Entry";
            default -> teamClassification();
        };
    }
}