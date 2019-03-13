package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.ClaysTeamAggregate;

import java.util.List;

public interface ClaysDataTeamRepository extends Repository<ClaysTeamAggregate, String> {
    List<ClaysTeamAggregate> getAllByClassificationOrderByTotalDesc(String classification);
}
