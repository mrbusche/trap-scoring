package trap.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "singlesaggregate")
public class SinglesAggregate {
    @EmbeddedId
    private SinglesAggregateIdentity singlesAggregateIdentity;
    @Column(name="team", insertable = false, updatable = false)
    String team;
    @Column(name="athlete", insertable = false, updatable = false)
    String athlete;
    String classification;
    String gender;
    Integer total;

    @Override
    public String toString() {
        return team + " - " + athlete + " - " + total;
    }
}
