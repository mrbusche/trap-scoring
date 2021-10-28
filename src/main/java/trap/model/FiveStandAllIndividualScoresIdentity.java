package trap.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
class FiveStandAllIndividualScoresIdentity implements Serializable {
    private String team;
    private String classification;
    private String athlete;
    private String type;
}
