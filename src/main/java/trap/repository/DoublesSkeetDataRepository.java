package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.DoublesSkeetAggregate;

import java.util.List;

public interface DoublesSkeetDataRepository extends Repository<DoublesSkeetAggregate, String> {
    List<DoublesSkeetAggregate> getAllByGenderAndClassification(String gender, String classification);
}
