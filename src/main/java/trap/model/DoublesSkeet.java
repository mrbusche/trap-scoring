package trap.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "doublesskeet")
public class DoublesSkeet extends TrapTypes {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;
}
