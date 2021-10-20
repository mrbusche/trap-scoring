package trap.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@Table(name = "claysaggregate")
public class ClaysAggregate {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ClaysAggregate that = (ClaysAggregate) o;
        return athlete != null && Objects.equals(athlete, that.athlete);
    }

    @Override
    public int hashCode() {
        return 0;
    }
}

