package proj.modular.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter @Getter
public class UserDto {
  @NotNull(message = "用户名不能为空")
  private String username;
  @NotNull(message = "密码不能为空")
  private String password;
  @NotNull(message = "名称不能为空")
  private String name;
  @NotNull(message = "警号不能为空")
  private String policeNum;
  private String deptId;
}
