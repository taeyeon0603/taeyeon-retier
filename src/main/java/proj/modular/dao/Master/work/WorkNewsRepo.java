package proj.modular.dao.Master.work;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.work.WorkNews;

@Repository
public interface WorkNewsRepo extends JpaRepository<WorkNews, String>, JpaSpecificationExecutor<WorkNews> {
}
