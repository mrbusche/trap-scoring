package trap.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "skeetTeamAggregate")
public class SkeetTeamAggregate {
    @Id
    String team;
    String classification;
    Integer total;

    @Override
    public String toString() {
        return team + " - " + total;
    }
}
