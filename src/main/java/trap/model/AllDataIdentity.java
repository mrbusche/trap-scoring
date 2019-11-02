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
class AllDataIdentity implements Serializable {
    private String team;
    private String eventid;
    private Integer locationid;
    private String eventdate;
    private String squadname;
    private String athlete;
    private String round1;
}
