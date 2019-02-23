package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.DoublesAggregate;

import java.util.List;

public interface DoublesDataRepository extends Repository<DoublesAggregate, String> {
    List<DoublesAggregate> getAllByGenderAndClassification(String gender, String classification);
}
