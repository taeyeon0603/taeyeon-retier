package proj.modular.vo.retire;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Setter @Getter
public class RetireVo {
  public RetireVo() {
//    familyMembers = new ArrayList<>(0);
    resumeList = new ArrayList<>(0);
    rewardList = new ArrayList<>(0);
    partyAge = 0;
    isHistory = false;
    activeRate = 0f;
    vaccinationList = new ArrayList<>(0);
    activitiesList = new ArrayList<>(0);
    remarkList = new ArrayList<>(0);
    visitList = new ArrayList<>(0);
    policeAge = 0;
    stayHere = true;
  }

  private String id;
  private String name;
  private String idCard;
  private Integer age;
  private String gender;
  private String policeCode;
  private String tel;
  private String shortTel;
  private String deptId;
  private String deptName;
  private String post;
  private String pic;
  private String level;
  private String party;
//  private List<FamilyMemberVo> familyMembers;
  private String residenceAddr;
  private String residenceType;
  private String policeStation;
  private String community;
  private String police;
  private String policeTel;
  private String healthStatus;
  private String fixedTel;
  private String politicalIdentity;
  private String disease;
  private String activities;
  private String pensionBankCard;
  private Date retireDate;
  private Date joinPartyDate;
  private Boolean isAlive;
  private Boolean isMasterAndApprentice;
  private Boolean isLonelyOldMan;
  private Boolean isLifeHard;
  private String remark;
  private List<ResumeVo> resumeList;
  private List<RewardVo> rewardList;

  private Date birth;
  private Integer partyAge;
  private Date moveInAt;
  private Date moveOutAt;
  private String moveOutDept;
  private String familyCondition;
  private String familyMember;
  private String familyTel;
  private Boolean isHistory;
  private Float activeRate;

  private List<VaccinationVo> vaccinationList;
  private List<ActivitiesVo> activitiesList;
  private List<RemarkVo> remarkList;
  private List<VisitVo> visitList;

  private Date workDate;
  private Integer policeAge;
  private Boolean stayHere;
  private Integer score;

  // 计算活跃度
  public void calculateActiveRate() {
    activeRate = 0f;

    if (vaccinationList.size() >= 3) {
      activeRate += 0.5f;
    }
    if (activitiesList.size() >= 1) {
      activeRate += 0.5f;
    }
    if (visitList.size() >= 2) {
      activeRate += 0.5f;
    }
    if (remarkList.size() >= 3) {
      activeRate += 0.5f;
    }
  }
}
