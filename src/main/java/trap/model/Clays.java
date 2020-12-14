package trap.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "clays")
public class Clays {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
    Integer CompId;
    String EventId;
    String Event;
    Integer LocationId;
    String Location;
    String EventDate;
    String SquadName;
    String Team;
    String Athlete;
    Integer AthleteId;
    String Classification;
    String Gender;
    Integer Round1;
    Integer Round2;
    Integer Round3;
    Integer Round4;
    Integer Round5;
    Integer Round6;
    Integer Round7;
    Integer Round8;
}
