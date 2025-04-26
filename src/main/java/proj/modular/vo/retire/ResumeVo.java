package proj.modular.vo.retire;

import lombok.Getter;
import lombok.Setter;
import proj.util.DateUtils;

import java.util.Date;

@Setter @Getter
public class ResumeVo {
  private String retireId;
  private String deptName;
  private Date startTime;
  private Date endTime;

  @Override
  public String toString() {
    return "在职单位:" + deptName +
      ", 开始于" + DateUtils.date2str(startTime, "yyyy年MM月dd日") +
      ", 结束于" + DateUtils.date2str(endTime, "yyyy年MM月dd日") +
      ";\r\n";
  }
}
