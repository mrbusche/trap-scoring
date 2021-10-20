package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.FiveStandData;

import java.util.List;

public interface FiveStandAllDataRepository extends Repository<FiveStandData, String> {
    List<FiveStandData> findAll();
}
