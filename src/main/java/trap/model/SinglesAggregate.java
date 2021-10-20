package trap.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "singlesaggregate")
public class SinglesAggregate {
    @Column(name = "team", insertable = false, updatable = false)
    String team;
    @Column(name = "athlete", insertable = false, updatable = false)
    String athlete;
    String classification;
    String gender;
    Integer total;
    @EmbeddedId
    private SinglesAggregateIdentity singlesAggregateIdentity;

    @Override
    public String toString() {
        return team + " - " + athlete + " - " + total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SinglesAggregate that = (SinglesAggregate) o;
        return singlesAggregateIdentity != null && Objects.equals(singlesAggregateIdentity, that.singlesAggregateIdentity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(singlesAggregateIdentity);
    }
}
