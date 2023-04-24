package trap.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RoundTotal {
    int eventId;
    int locationId;
    String team;
    String athlete;
    String classification;
    String gender;
    int total;
    String type;

    public String getUniqueName() {
        return this.getAthlete() + " " + this.getTeam() + " " + this.getClassification() + " " + this.getType();
    }
}
