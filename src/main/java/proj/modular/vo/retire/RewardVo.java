package proj.modular.vo.retire;

import lombok.Getter;
import lombok.Setter;
import proj.util.DateUtils;

import java.util.Date;

@Setter @Getter
public class RewardVo {
  private Integer id;
  private String retireId;
  private String policeCode;
  private String dept;
  private String codeNumber;
  private String rewardType;
  private String types; //集体 or 个人
  private String linkAddress;
  private String approveAddress;
  private String approveLevel;
  private Date rewardDate;
  private Date createAt;
  private String createBy;

  @Override
  public String toString() {
    return "文书号:" + codeNumber +
      ", 获奖类型:" + rewardType +
      ", 发文标题:" + linkAddress +
      ", 批准机关:" + approveAddress +
      ", 批准级别:" + approveLevel +
      ", 获奖日期:" + DateUtils.date2str(rewardDate, "yyyy年MM月dd日") +
      ";\r\n";
  }
}
