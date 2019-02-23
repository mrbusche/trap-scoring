package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.SinglesAggregate;

import java.util.List;

public interface SinglesDataRepository extends Repository<SinglesAggregate, String> {
    List<SinglesAggregate> getAllByGenderAndClassification(String gender, String classification);
}
