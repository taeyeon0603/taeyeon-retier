package proj.modular.vo.retire;

import lombok.Getter;
import lombok.Setter;
import proj.util.DateUtils;

import java.util.Date;

@Setter @Getter
public class ActivitiesVo {
  private String name;
  private String addr;
  private Date eventDate;

  @Override
  public String toString() {
    return "活动名:" + name +
      ", 活动地点:" + addr +
      ", 活动时间:" + DateUtils.date2str(eventDate, "yyyy年MM月dd日") + ";\r\n";
  }
}
