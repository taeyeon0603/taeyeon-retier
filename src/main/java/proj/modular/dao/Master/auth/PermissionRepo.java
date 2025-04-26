package proj.modular.dao.Master.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.auth.Permission;

import java.util.List;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, Integer> {
  Permission findByNameAndGroupName(String name, String groupName);
  List<Permission> findAllByIdIn(List<Integer> permissionIds);
}
