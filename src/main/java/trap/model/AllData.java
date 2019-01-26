package trap.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "alldata")
public class AllData {
    @Id
    String eventid;
    String event;
    Integer locationid;
    String location;
    String eventdate;
    String squadname;
    Integer station;
    String team;
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
