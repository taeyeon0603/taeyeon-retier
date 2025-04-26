package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.Log;

@Repository
public interface LogRepo extends JpaRepository<Log, String>, JpaSpecificationExecutor<Log> {

}
