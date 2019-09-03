package trap.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
class AllTeamScoresIdentity implements Serializable {
    private String team;
    private String classification;
    private String athlete;
    private String type;

    public AllTeamScoresIdentity() {

    }

    public AllTeamScoresIdentity(String team, String classification, String athlete, String type) {
        this.team = team;
        this.classification = classification;
        this.athlete = athlete;
        this.type = type;
    }
}
