package proj.modular.dao.Master.work;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.work.WorkInfo;

@Repository
public interface WorkInfoRepo extends JpaRepository<WorkInfo, String>, JpaSpecificationExecutor<WorkInfo> {

}
