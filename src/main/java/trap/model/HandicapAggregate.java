package trap.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "handicapaggregate")
public class HandicapAggregate {
    String team;
    @Id
    String athlete;
    String classification;
    String gender;
    Integer total;

    @Override
    public String toString() {
        return athlete + " - " + team + " - " + total;
    }
}
