package trap.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "singlesteamaggregate")
public class SinglesTeamAggregate {
    @Id
    String team;
    String classification;
    String gender;
    Integer total;

    @Override
    public String toString() {
        return team + " - " + total;
    }
}
