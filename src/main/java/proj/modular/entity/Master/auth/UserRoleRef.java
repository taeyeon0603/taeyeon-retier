package proj.modular.entity.Master.auth;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "user_role_ref")
@Setter @Getter
public class UserRoleRef {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "user_id")
  private String userId;
  @Column(name = "role_id")
  private Integer roleId;
}
