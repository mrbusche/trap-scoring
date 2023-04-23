package trap.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class IndividualTotal {
    int locationId;
    String team;
    String athlete;
    String classification;
    String gender;
    int total;
    String type;
}
