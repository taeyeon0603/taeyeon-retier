package proj.modular.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class UserPageDto extends PageBase{
  private String username;
  private String name;
  private String deptName;
}
