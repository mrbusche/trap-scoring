package trap.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "allteamscores")
public class AllTeamScores {
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
}
