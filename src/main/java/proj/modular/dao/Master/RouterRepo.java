package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.Router;

import java.util.List;

@Repository
public interface RouterRepo extends JpaRepository<Router, String>, JpaSpecificationExecutor<Router> {
  List<Router> findAllByIdIn(List<String> routerIds);
}
