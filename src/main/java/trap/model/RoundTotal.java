package trap.model;

public record RoundTotal(
        int eventId,
        int locationId,
        String team,
        String athlete,
        String classification,
        String gender,
        int total,
        String type
) {
}
