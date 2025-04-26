package proj.modular.vo;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class CheckablePermission {
  private Integer id;
  private String name;
  private String groupKey;
  private String groupName;
  private boolean isChecked;
}
