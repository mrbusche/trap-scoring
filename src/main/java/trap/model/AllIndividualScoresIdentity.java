package trap.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@RequiredArgsConstructor
class AllIndividualScoresIdentity implements Serializable {
    private final String team;
    private final String classification;
    private final String athlete;
    private final String type;
}
