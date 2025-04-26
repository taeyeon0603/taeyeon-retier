package proj.modular.dao.Master.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.auth.RolePermissionRef;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface RolePermissionRepo extends JpaRepository<RolePermissionRef, Integer> {
  @Transactional
  void deleteByRoleId(Integer roleId);
  List<RolePermissionRef> findAllByRoleIdIn(List<Integer> roleIds);
}
