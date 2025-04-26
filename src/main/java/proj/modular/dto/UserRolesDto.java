package proj.modular.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class UserRolesDto {
  private String userId;
  private List<Integer> roles;
}
