package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.Resume;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ResumeRepo extends JpaRepository<Resume, String> {
  @Transactional
  void deleteByRetireId(String retireId);
  List<Resume> findAllByRetireIdIn(List<String> retireIds);
}
