package trap.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

@AllArgsConstructor
@Data
public class IndividualTotal {
    int locationId;
    @Setter
    String team;
    String athlete;
    String classification;
    String gender;
    int total;
    String type;

    public String getTeamForScores() {
        return "%s %s %s".formatted(this.type, this.team, this.getTeamClassification());
    }

    public String getTeamClassification() {
        return switch (classification) {
            case "Senior/Varsity", "Senior/Jr. Varsity", "Junior Varsity" -> "Varsity";
            case "Intermediate/Advanced", "Intermediate/Entry Level" -> "Intermediate Entry";
            default -> classification;
        };
    }
}