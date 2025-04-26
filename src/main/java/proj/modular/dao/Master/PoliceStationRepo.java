package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.PoliceStation;

@Repository
public interface PoliceStationRepo extends JpaRepository<PoliceStation, Integer> {
  PoliceStation findByName(String policeStation);
}
