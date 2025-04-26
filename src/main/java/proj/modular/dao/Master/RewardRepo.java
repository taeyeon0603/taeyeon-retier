package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.Reward;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RewardRepo extends JpaRepository<Reward, Integer> {
  @Transactional
  void deleteAllByRetireId(String retireId);
  List<Reward> findAllByRetireIdIn(List<String> retireIds);
}
