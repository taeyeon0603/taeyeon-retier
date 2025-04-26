package proj.modular.vo.retire;

import lombok.Getter;
import lombok.Setter;
import proj.util.DateUtils;

import java.util.Date;

@Setter @Getter
public class VisitVo {
  private String addr;
  private String content;
  private Date visitDate;
  private String visitDept;

  @Override
  public String toString() {
    return "走访单位:'" + visitDept +
      ", 走访地点:" + addr +
      ", 主题:'" + content +
      ", 日期:" + DateUtils.date2str(visitDate, "yyyy年MM月dd日") +
      ";\r\n";
  }
}
