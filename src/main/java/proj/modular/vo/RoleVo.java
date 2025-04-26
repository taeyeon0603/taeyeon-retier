package proj.modular.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter @Getter
public class RoleVo {
  private Integer id;
  private String name;
  private Date createAt;
  private String createBy;
  private Date updateAt;
  private String updateBy;
  private List<CascadeVo> permissionGroup;
}
