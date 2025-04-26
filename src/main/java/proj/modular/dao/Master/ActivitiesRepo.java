package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.Activities;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ActivitiesRepo extends JpaRepository<Activities, Integer> {
  @Transactional
  void deleteAllByRetireId(String retireId);
  List<Activities> findAllByRetireIdIn(List<String> retireIds);
}
