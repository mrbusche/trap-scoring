package trap.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "allIndividualScores")
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
}
