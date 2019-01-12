package trap.repository;

import org.springframework.data.repository.Repository;

import trap.model.HandicapTeamAggregate;
import java.util.List;


public interface HandicapDataTeamRepository extends Repository<HandicapTeamAggregate, String> {
    List<HandicapTeamAggregate> getAllByClassificationAndGenderOrderByTotalDesc(String classification, String gender);
}
