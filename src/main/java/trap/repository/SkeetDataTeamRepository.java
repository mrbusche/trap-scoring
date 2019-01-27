package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.SkeetTeamAggregate;

import java.util.List;

public interface SkeetDataTeamRepository extends Repository<SkeetTeamAggregate, String> {
    List<SkeetTeamAggregate> getAllByClassificationOrderByTotalDesc(String classification);
}
