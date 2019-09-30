package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.AllIndividualScores;

import java.util.List;

public interface AllIndividualScoresRepository extends Repository<AllIndividualScores, String> {
    List<AllIndividualScores> findAll();
}
