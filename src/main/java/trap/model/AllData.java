package trap.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "alldata")
public class AllData {
    @Column(name = "eventid", insertable = false, updatable = false)
    String eventid;
    String event;
    @Column(name = "locationid", insertable = false, updatable = false)
    Integer locationid;
    String location;
    @Column(name = "eventdate", insertable = false, updatable = false)
    String eventdate;
    @Column(name = "squadname", insertable = false, updatable = false)
    String squadname;
    @Column(name = "team", insertable = false, updatable = false)
    String team;
    @Column(name = "athlete", insertable = false, updatable = false)
    String athlete;
    String classification;
    String gender;
    @Column(name = "round1", insertable = false, updatable = false)
    Integer round1 = 0;
    Integer round2 = 0;
    Integer round3 = 0;
    Integer round4 = 0;
    Integer round5 = 0;
    Integer round6 = 0;
    Integer round7 = 0;
    Integer round8 = 0;
    String fivestand;
    String type;
    @EmbeddedId
    private AllDataIdentity allDataIdentity;
}
