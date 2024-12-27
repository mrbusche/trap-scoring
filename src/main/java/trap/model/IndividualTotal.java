package trap.model;

import trap.common.Classifications;

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
            case "Senior/Varsity" -> Classifications.VARSITY;
            case "Senior/Jr. Varsity", Classifications.JUNIOR_VARSITY -> Classifications.JUNIOR_VARSITY;
            case "Intermediate/Advanced" -> "Intermediate Advanced";
            case "Intermediate/Entry Level" -> "Intermediate Entry";
            default -> classification;
        };
    }

    public String teamClassificationForTotal() {
        return switch (teamClassification()) {
            case "Senior/Varsity", "Senior/Jr. Varsity", Classifications.VARSITY, Classifications.JUNIOR_VARSITY -> Classifications.VARSITY;
            case "Intermediate Advanced" -> "Intermediate Entry";
            default -> teamClassification();
        };
    }
}
