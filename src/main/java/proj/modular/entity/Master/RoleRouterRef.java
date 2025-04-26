package proj.modular.entity.Master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "role_router_ref")
@Setter @Getter
public class RoleRouterRef {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "role_id")
  private Integer roleId;
  @Column(name = "router_id")
  private String routerId;
}
