package trap.model;

public record TrapRoundScore(
        int eventId,
        int locationId,
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
    public TrapRoundScore(RoundScore s) {
        this(s.eventId(), s.locationId(), s.team(), s.athlete(), s.classification(), s.gender(), s.round1(), s.round2(), s.round3(), s.round4(), s.round5(), s.round6(), s.round7(), s.round8(), s.type());
    }

    public String uniqueName() {
        return athlete + " " + team + " " + classification + " " + type;
    }
}
