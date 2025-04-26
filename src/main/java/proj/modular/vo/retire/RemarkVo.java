package proj.modular.vo.retire;

import lombok.Getter;
import lombok.Setter;
import proj.util.DateUtils;

import java.util.Date;

@Setter @Getter
public class RemarkVo {
  private String addr;
  private String content;
  private Date eventDate;

  @Override
  public String toString() {
    return "地点:" + addr +
      ", 内容:" + content +
      ", 时间:" + DateUtils.date2str(eventDate, "yyyy年MM月dd日") + ";\r\n";
  }
}
