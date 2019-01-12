package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.SinglesTeamAggregate;

import java.util.List;

public interface SinglesDataTeamRepository extends Repository<SinglesTeamAggregate, String> {
    List<SinglesTeamAggregate> getAllByClassificationAndGenderOrderByTotalDesc(String classification, String gender);
}
