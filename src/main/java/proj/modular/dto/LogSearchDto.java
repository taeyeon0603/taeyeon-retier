package proj.modular.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
public class LogSearchDto extends PageBase {
  private String deptId;
  private String optUserName;
  private Date start;
  private Date end;
}
