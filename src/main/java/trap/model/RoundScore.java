package trap.model;

public record RoundScore(
        int eventId,
        String event,
        int locationId,
        String location,
        String eventDate,
        String squadName,
        String team,
        String athlete,
        String classification,
        String gender,
        int round1,
        int round2,
        int round3,
        int round4,
        int round5,
        int round6,
        int round7,
        int round8,
        String type
) {

    public String uniqueName() {
        return athlete + " " + team + " " + classification + " " + type;
    }

    public String teamClassification() {
        return switch (classification) {
            case "Senior/Varsity" -> "Varsity";
            case "Senior/Jr. Varsity" -> "Senior Varsity";
            case "Intermediate/Advanced" -> "Intermediate Advanced";
            case "Intermediate/Entry Level" -> "Intermediate Entry";
            default -> classification;
        };
    }
}
