package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.RoleRouterRef;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Repository
public interface RoleRouterRepo extends JpaRepository<RoleRouterRef, Integer> {
  List<RoleRouterRef> findAllByRoleIdIn(Set<Integer> roleIds);
  List<RoleRouterRef> findAllByRoleId(Integer roleId);
  @Transactional
  void deleteByRoleId(Integer roleId);
}
