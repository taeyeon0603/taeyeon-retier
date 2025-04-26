package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.Dict;

import java.util.List;

@Repository
public interface DictRepo extends JpaRepository<Dict, Integer> {
  List<Dict> findAllByType(String type);
}
