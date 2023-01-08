package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.DoublesSkeetTeamAggregate;

import java.util.List;

public interface DoublesSkeetDataTeamRepository extends Repository<DoublesSkeetTeamAggregate, String> {
    List<DoublesSkeetTeamAggregate> getAllByClassificationOrderByTotalDesc(String classification);
}
