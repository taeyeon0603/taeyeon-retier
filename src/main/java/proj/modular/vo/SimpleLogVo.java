package proj.modular.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
public class SimpleLogVo {
  private String terminalIP;
  private String userName;
  private String deptName;
  private String content;
  private Date optTime;
}
