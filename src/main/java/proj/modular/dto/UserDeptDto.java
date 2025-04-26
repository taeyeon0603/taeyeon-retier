package proj.modular.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Setter @Getter
public class UserDeptDto {
  @NotNull(message = "用户不能为空")
  private String userId;
  private Set<String> deptIds;
}
