package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.FiveStandAllIndividualScores;

import java.util.List;

public interface FiveStandAllIndividualScoresRepository extends Repository<FiveStandAllIndividualScores, String> {
    List<FiveStandAllIndividualScores> findAllByOrderByTeamAscTypeAscClassificationAscGenderAscTotalDesc();
}
