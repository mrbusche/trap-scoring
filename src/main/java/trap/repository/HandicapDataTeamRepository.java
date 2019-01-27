package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.HandicapTeamAggregate;

import java.util.List;

public interface HandicapDataTeamRepository extends Repository<HandicapTeamAggregate, String> {
    List<HandicapTeamAggregate> getAllByClassificationOrderByTotalDesc(String classification);
}
