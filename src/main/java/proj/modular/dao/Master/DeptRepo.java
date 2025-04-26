package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.Dept;

@Repository
public interface DeptRepo extends JpaRepository<Dept, String> {
  Dept findByDeptId(String deptId);
}
