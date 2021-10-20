package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.FiveStandAggregate;

import java.util.List;

public interface FiveStandDataRepository extends Repository<FiveStandAggregate, String> {
    List<FiveStandAggregate> getAllByGenderAndClassification(String gender, String classification);
}
