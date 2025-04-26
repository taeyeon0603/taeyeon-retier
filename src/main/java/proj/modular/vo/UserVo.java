package proj.modular.vo;

import lombok.Getter;
import lombok.Setter;
import proj.modular.entity.Master.PoliceStation;
import proj.modular.entity.Master.auth.Role;

import java.util.List;
import java.util.Set;

@Setter @Getter
public class UserVo {
  private String id;
  private String username;
  private String name;
  private String policeNum;
  private String deptId;
  private String deptName;
  private List<Role> roles;
  private Set<PoliceStation> manageDepts;
}
