package proj.modular.dao.Master.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.auth.Role;

import java.util.Collection;
import java.util.List;

@Repository
public interface RoleRepo extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {
  Role findOneByName(String roleName);
  List<Role> findAllByIdIn(Collection<Integer> roleIds);
}
