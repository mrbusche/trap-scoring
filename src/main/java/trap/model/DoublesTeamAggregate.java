package trap.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "doublesteamaggregate")
public class DoublesTeamAggregate {
    @Id
    String team;
    String classification;
    Integer total;

    @Override
    public String toString() {
        return team + " - " + total;
    }
}
