package proj.modular.entity.Master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "user")
@Setter @Getter
public class User implements Serializable {
  @Id
  private String id;
  private String username;
  private String name;
  @Column(name = "police_num")
  private String policeNum;
  private String pwd;
  @Column(name = "dept_id")
  private String deptId;
  @Column(name = "dept_name")
  private String deptName;
  @Column(name = "create_by")
  private String createBy;
  @Column(name = "create_at")
  private Date createAt;
  @Column(name = "update_by")
  private String updateBy;
  @Column(name = "update_at")
  private Date updateAt;
  @Column(name = "is_deleted", columnDefinition = "bit(1) default b'0'")
  private Boolean isDeleted;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
      name = "user_view_dept",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "dept_id")
  )
  private Set<Dept> viewDepts;

  @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  @JoinTable(
      name = "user_manage_dept",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "police_station_id")
  )
  private Set<PoliceStation> manageDepts;

  public static User temp() {
    User tmpUser = new User();
    tmpUser.setName("未知用户");
    return tmpUser;
  }
}
