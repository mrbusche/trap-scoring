package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.FiveStandAllData;

import java.util.List;

public interface FiveStandAllDataRepository extends Repository<FiveStandAllData, String> {
    List<FiveStandAllData> findAll();
}
