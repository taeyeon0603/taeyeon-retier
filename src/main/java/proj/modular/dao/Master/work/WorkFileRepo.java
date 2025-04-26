package proj.modular.dao.Master.work;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.work.WorkFile;

import java.util.List;

@Repository
public interface WorkFileRepo extends JpaRepository<WorkFile, String>, JpaSpecificationExecutor<WorkFile> {
  List<WorkFile> findAllByRefId(String id);
  List<WorkFile> findAllByRefIdIsNull();
  List<WorkFile> findAllByRefIdIn(List<String> infoIds);
}
