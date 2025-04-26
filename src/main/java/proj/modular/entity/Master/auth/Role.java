package proj.modular.entity.Master.auth;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "role")
@Proxy(lazy = false)
@Setter @Getter
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  @Column(name = "create_at")
  private Date createAt;
  @Column(name = "create_by")
  private String createBy;
  @Column(name = "update_at")
  private Date updateAt;
  @Column(name = "update_by")
  private String updateBy;
}
