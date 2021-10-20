package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.FiveStandTeamAggregate;

import java.util.List;

public interface FiveStandDataTeamRepository extends Repository<FiveStandTeamAggregate, String> {
    List<FiveStandTeamAggregate> getAllByClassificationOrderByTotalDesc(String classification);
}
