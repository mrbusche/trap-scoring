package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.DoublesTeamAggregate;

import java.util.List;

public interface DoublesDataTeamRepository extends Repository<DoublesTeamAggregate, String> {
    List<DoublesTeamAggregate> getAllByClassificationOrderByTotalDesc(String classification);
}
