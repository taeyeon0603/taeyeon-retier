package proj.modular.dao.Master;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.Visit;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface VisitRepo extends JpaRepository<Visit, Integer> {
  @Transactional
  void deleteAllByRetireId(String retireId);
  List<Visit> findAllByRetireIdIn(List<String> retireIds);
  @Query("select v from Visit v where v.retireId in (?0) order by v.visitDate desc") //TODO 后续修改
  List<Visit> getLatestRecords(List<String> retireIds, Pageable pageable);
}
