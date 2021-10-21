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
@Table(name = "fivestand")
public class FiveStandData {
    @Column(name = "eventid", insertable = false, updatable = false)
    String eventid;
    String event;
    @Column(name = "locationid", insertable = false, updatable = false)
    Integer locationid;
    String location;
    @Column(name = "eventdate", insertable = false, updatable = false)
    String eventdate;
    @Column(name = "squadname", insertable = false, updatable = false)
    String squadname;
    @Column(name = "team", insertable = false, updatable = false)
    String team;
    @Column(name = "athlete", insertable = false, updatable = false)
    String athlete;
    String classification;
    String gender;
    @Column(name = "round1", insertable = false, updatable = false)
    Integer round1 = 0;
    Integer round2 = 0;
    Integer round3 = 0;
    Integer round4 = 0;
    Integer round5 = 0;
    Integer round6 = 0;
    Integer round7 = 0;
    Integer round8 = 0;
    //    String type = "fivestand";
    @EmbeddedId
    private FiveStandDataIdentity fiveStandDataIdentity;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FiveStandData that = (FiveStandData) o;
        return fiveStandDataIdentity != null && Objects.equals(fiveStandDataIdentity, that.fiveStandDataIdentity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fiveStandDataIdentity);
    }
}
