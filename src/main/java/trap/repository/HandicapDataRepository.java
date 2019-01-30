package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.HandicapAggregate;

import java.util.List;

public interface HandicapDataRepository extends Repository<HandicapAggregate, String> {
    List<HandicapAggregate> getAllByClassificationAndGenderOrderByTotalDescAthleteAsc(String classification, String gender);

    List<HandicapAggregate> getAllByTeam(String team);

    List<HandicapAggregate> getAllByGender(String gender);
}
