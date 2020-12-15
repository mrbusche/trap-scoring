package trap.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "singles")
public class Singles extends TrapTypes {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
}
