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
    @EmbeddedId
    private AllDataIdentity allDataIdentity;

    @Column(name="eventid", insertable = false, updatable = false)
    String eventid;
    String event;
    @Column(name="locationid", insertable = false, updatable = false)
    Integer locationid;
    String location;
    @Column(name="eventdate", insertable = false, updatable = false)
    String eventdate;
    @Column(name="squadname", insertable = false, updatable = false)
    String squadname;
    Integer station;
    @Column(name="team", insertable = false, updatable = false)
    String team;
    @Column(name="athlete", insertable = false, updatable = false)
    String athlete;
    String classification;
    String gender;
    Integer round1;
    Integer round2;
    Integer round3;
    Integer round4;
    Integer round5;
    Integer round6;
    Integer round7;
    Integer round8;
    Integer frontrun;
    Integer backrun;
    String registerdate;
    String registeredby;
    String shirtsize;
    String ataid;
    String nssaid;
    String nscaid;
    String sctppayment;
    String sctpconsent;
    String atapayment;
    String nscapayment;
    String nssapayment;
    String type;

}
