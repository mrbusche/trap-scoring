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
@Table(name = "fivestandallteamscores")
public class FiveStandAllTeamScores {
    @Column(name = "team", insertable = false, updatable = false)
    String team;
    @Column(name = "classification", insertable = false, updatable = false)
    String classification;
    @Column(name = "athlete", insertable = false, updatable = false)
    String athlete;
    Integer indtotal;
    Integer teamtotal;
    @Column(name = "type", insertable = false, updatable = false)
    String type;
    @EmbeddedId
    private AllTeamScoresIdentity allTeamScoresIdentity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FiveStandAllTeamScores that = (FiveStandAllTeamScores) o;
        return allTeamScoresIdentity != null && Objects.equals(allTeamScoresIdentity, that.allTeamScoresIdentity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(allTeamScoresIdentity);
    }
}
