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
@Table(name = "fivestandallindividualscores")
public class FiveStandAllIndividualScores {
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
    private FiveStandAllIndividualScoresIdentity fiveStandAllIndividualScoresIdentity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FiveStandAllIndividualScores that = (FiveStandAllIndividualScores) o;
        return fiveStandAllIndividualScoresIdentity != null && Objects.equals(fiveStandAllIndividualScoresIdentity, that.fiveStandAllIndividualScoresIdentity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fiveStandAllIndividualScoresIdentity);
    }
}
