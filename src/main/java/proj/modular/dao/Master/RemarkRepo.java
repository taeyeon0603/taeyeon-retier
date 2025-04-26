package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.Remark;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RemarkRepo extends JpaRepository<Remark, Integer> {
  @Transactional
  void deleteAllByRetireId(String retireId);
  List<Remark> findAllByRetireIdIn(List<String> retireIds);
}