package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.Vaccination;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface VaccinationRepo extends JpaRepository<Vaccination, Integer> {
  @Transactional
  void deleteAllByRetireId(String id);
  List<Vaccination> findAllByRetireIdIn(List<String> retireIds);
}
