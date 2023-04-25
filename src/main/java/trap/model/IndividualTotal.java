package trap.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class IndividualTotal {
    int locationId;
    String team;
    String athlete;
    String classification;
    String gender;
    int total;
    String type;

    public void setTeam(String team) {
        this.team = team;
    }

    public String getTeamForScores() {
        return this.type + " " + this.team + " " + this.getTeamClassification();
    }

    public String getTeamClassification() {
        return this.classification.replace("Senior/Jr. Varsity", "Varsity").replace("Senior/Varsity", "Varsity").replace("Junior Varsity", "Varsity").replace("Intermediate Advanced", "Intermediate Entry");
    }
}
