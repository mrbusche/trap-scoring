package trap.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "fivestandaggregate")
public class FiveStandAggregate {
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

