package trap.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
class SinglesAggregateIdentity implements Serializable {
    private String team;
    private String athlete;

    public SinglesAggregateIdentity() {

    }

    public SinglesAggregateIdentity(String team, String athlete) {
        this.team = team;
        this.athlete = athlete;
    }
}
