package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.ClaysAggregate;

import java.util.List;

public interface ClaysDataRepository extends Repository<ClaysAggregate, String> {
    List<ClaysAggregate> getAllByGenderAndClassification(String gender, String classification);
}
