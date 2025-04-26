package proj.modular.vo.retire;

import lombok.Getter;
import lombok.Setter;
import proj.util.DateUtils;

import java.util.Date;

@Setter @Getter
public class VaccinationVo {
  private String batchNo;
  private String inoculateObj;
  private Date inoculateAt;
  private String inoculateAddr;
  private String type;

  @Override
  public String toString() {
    return batchNo +
      ", 疫苗类型:" + type +
      ", 接种对象:" + inoculateObj +
      ", 接种时间:" + DateUtils.date2str(inoculateAt, "yyyy年MM月dd日") +
      ", 接种地点:" + inoculateAddr + ";\r\n";
  }
}
