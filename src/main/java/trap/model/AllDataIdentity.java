package trap.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@RequiredArgsConstructor
class AllDataIdentity implements Serializable {
    private final String team;
    private final String eventid;
    private final Integer locationid;
    private final String eventdate;
    private final String squadname;
    private final String athlete;
    private final String round1;
}
