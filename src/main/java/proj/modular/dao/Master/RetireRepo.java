package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.RetireInfo;

@Repository
public interface RetireRepo extends JpaRepository<RetireInfo, Integer> {
}
