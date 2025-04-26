package proj.modular.dao.Master.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.auth.UserRoleRef;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRoleRef, Integer> {
  @Transactional
  void deleteByUserId(String userId);
  List<UserRoleRef> findAllByUserId(String userId);
}
