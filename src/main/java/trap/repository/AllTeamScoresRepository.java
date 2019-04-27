package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.AllTeamScores;

import java.util.List;

public interface AllTeamScoresRepository extends Repository<AllTeamScores, String> {
    List<AllTeamScores> findAll();
}
