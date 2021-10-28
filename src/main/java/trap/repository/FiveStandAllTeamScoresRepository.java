package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.FiveStandAllTeamScores;

import java.util.List;

public interface FiveStandAllTeamScoresRepository extends Repository<FiveStandAllTeamScores, String> {
    List<FiveStandAllTeamScores> findAll();
}
