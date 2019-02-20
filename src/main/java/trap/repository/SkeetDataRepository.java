package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.SkeetAggregate;

import java.util.List;

public interface SkeetDataRepository extends Repository<SkeetAggregate, String> {
    List<SkeetAggregate> getAllByGender(String gender);

    List<SkeetAggregate> getAllByGenderAndClassification(String gender, String classification);
}
