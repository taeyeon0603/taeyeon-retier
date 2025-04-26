package proj.modular.dto.retire;

import lombok.Getter;
import lombok.Setter;
import proj.modular.dto.PageBase;

@Setter @Getter
public class RetirePageDto extends PageBase {
  private String name;
  private String idCard;
  private String dept;
  private Boolean isHistory;

  private String politicalIdentity;
  private Integer ageStart;
  private Integer ageEnd;
  private String partyBranch;
  private String gender;
  private Float activeRate;
  private String residencePoliceStation;

  private String residenceType;
  private Integer policeAgeStart;
  private Integer policeAgeEnd;
  private Integer partyAgeStart;
  private Integer partyAgeEnd;
}
