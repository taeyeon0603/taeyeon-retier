package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.ResidenceInfo;

@Repository
public interface ResidenceInfoRepo extends JpaRepository<ResidenceInfo, Integer>, JpaSpecificationExecutor<ResidenceInfo> {
//  Collection<ResidenceInfo> findAllByIdIn(Set<Integer> residenceRefId);
//  List<ResidenceInfo> findAllByPoliceStation(String policeStation);
}
