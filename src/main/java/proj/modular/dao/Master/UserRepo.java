package proj.modular.dao.Master;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import proj.modular.entity.Master.User;

@Repository
public interface UserRepo extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
  User findByUsername(String username);
  User findByPoliceNum(String policeNum);
}
