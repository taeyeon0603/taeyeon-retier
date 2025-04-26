package proj.modular.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter @Setter
public class RolePermissionsDto {
  @NotNull(message = "角色不能为空")
  private Integer roleId;
  private List<Integer> permissions;
}
