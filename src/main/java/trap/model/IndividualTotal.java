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
        return this.type + " " + this.team + " " + this.getTeamClassificationForTotal();
    }

    public String getTeamClassification() {
        return this.classification.replace("Senior/Varsity", "Varsity").replace("Senior/Jr. Varsity", "Junior Varsity").replace("Intermediate/Advanced", "Intermediate Advanced").replace("Intermediate/Entry Level", "Intermediate Entry");
    }

    public String getTeamClassificationForTotal() {
        return this.getTeamClassification().replace("Senior/Jr. Varsity", "Varsity").replace("Senior/Varsity", "Varsity").replace("Junior Varsity", "Varsity").replace("Intermediate Advanced", "Intermediate Entry");
    }
}
