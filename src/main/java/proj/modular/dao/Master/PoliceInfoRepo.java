package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.PoliceInfo;

@Repository
public interface PoliceInfoRepo extends JpaRepository<PoliceInfo, String>, JpaSpecificationExecutor<PoliceInfo> {
}
