package proj.modular.dto.retire;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class RetireSearchDto {
  private String name;
  private String idCard;
  private String dept;
  private Boolean isHistory;
}
