package proj.modular.entity.Master.auth;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "permission")
@Setter @Getter
public class Permission {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  private String uri;
  @Column(name = "group_name")
  private String groupName;
  @Column(name = "create_at")
  private Date createAt;
  @Column(name = "update_at")
  private Date updateAt;
  @Column(name = "create_by")
  private String createBy;
  @Column(name = "update_by")
  private String updateBy;

  public String getGroupKey() {
    int ind = uri.indexOf(":");
    return ind > -1? uri.substring(0, ind): "unknown";
  }
}
