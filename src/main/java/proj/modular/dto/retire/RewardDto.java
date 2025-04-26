package proj.modular.dto.retire;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
public class RewardDto {
  private Integer id;
  private String dept;
  private String codeNumber;
  private String rewardType;
  private String types; //?
  private String linkAddress;
  private String approveAddress;
  private String approveLevel;
  private Date rewardDate;
}
