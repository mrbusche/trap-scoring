package trap.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "doubles")
public class Doubles {
    Integer compId;
    String eventId;
    String event;
    Integer locationId;
    String location;
    String eventDate;
    String squadName;
    String team;
    String athlete;
    Integer athleteId;
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
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
}
