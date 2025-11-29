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
        return "%s %s %s %s".formatted(athlete, team, classification, type);
    }
}
