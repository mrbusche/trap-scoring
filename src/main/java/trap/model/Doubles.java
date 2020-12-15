package trap.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "doubles")
public class Doubles extends TrapTypes {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
}
