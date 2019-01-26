package trap.repository;

import org.springframework.data.repository.Repository;
import trap.model.AllData;

import java.util.List;

public interface AllDataRepository extends Repository<AllData, String> {
    List<AllData> findAll();
}
