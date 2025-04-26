package proj.modular.entity.Master.auth;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "role_permission_ref")
@Setter @Getter
public class RolePermissionRef {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "role_id")
  private Integer roleId;
  @Column(name = "permission_id")
  private Integer permissionId;
}
