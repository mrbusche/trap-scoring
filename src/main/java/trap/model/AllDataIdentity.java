package trap.model;

import lombok.Data;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class AllDataIdentity implements Serializable {
    private String eventid;
    private Integer locationid;
    private String eventdate;
    private String squadname;
    String team;
    private String athlete;

    public AllDataIdentity() {

    }

    public AllDataIdentity(String eventid, Integer locationid, String eventdate, String squadname, String team, String athlete) {
        this.eventid = eventid;
        this.locationid = locationid;
        this.eventdate = eventdate;
        this.squadname = squadname;
        this.team = team;
        this.athlete = athlete;
    }
}
