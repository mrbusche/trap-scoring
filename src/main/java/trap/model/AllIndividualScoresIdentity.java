package trap.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
class AllIndividualScoresIdentity implements Serializable {
    private String team;
    private String classification;
    private String athlete;
    private String type;

    public AllIndividualScoresIdentity() {

    }

    public AllIndividualScoresIdentity(String team, String classification, String athlete, String type) {
        this.team = team;
        this.classification = classification;
        this.athlete = athlete;
        this.type = type;
    }
}
