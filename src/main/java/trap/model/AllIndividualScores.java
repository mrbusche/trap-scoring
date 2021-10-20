package trap.model;

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
@Table(name = "allindividualscores")
public class AllIndividualScores {
    @Column(name = "team", insertable = false, updatable = false)
    String team;
    @Column(name = "classification", insertable = false, updatable = false)
    String classification;
    @Column(name = "athlete", insertable = false, updatable = false)
    String athlete;
    @Column(name = "gender", insertable = false, updatable = false)
    String gender;
    @Column(name = "total", insertable = false, updatable = false)
    Integer total;
    @Column(name = "type", insertable = false, updatable = false)
    String type;
    @EmbeddedId
    private AllIndividualScoresIdentity allIndividualScoresIdentity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AllIndividualScores that = (AllIndividualScores) o;
        return allIndividualScoresIdentity != null && Objects.equals(allIndividualScoresIdentity, that.allIndividualScoresIdentity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allIndividualScoresIdentity);
    }
}
