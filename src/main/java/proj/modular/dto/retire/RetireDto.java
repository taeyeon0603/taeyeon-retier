package proj.modular.dto.retire;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import proj.modular.entity.Master.*;

import javax.validation.constraints.NotNull;
import java.util.*;

@Setter @Getter
public class RetireDto {
  private String id;
  @NotNull(message = "姓名不能为空")
  private String name;
  @NotNull(message = "身份证不能为空")
  private String idCard;
  private String gender;
  private String policeCode;
  private String tel;
  private String shortTel;
  @NotNull(message = "原单位不能为空")
  private String deptId;
  @NotNull(message = "原单位不能为空")
  private String deptName;
  private String post;
  private String pic;
  private String level;
//  @NotNull(message = "退休党支部不能为空")
  private String party;
//  private List<FamilyMemberDto> familyMembers;
  private String residenceAddr;
  private String residenceType;
  private Integer policeStationId;
  @NotNull(message = "辖区派出所不能为空")
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
  private Boolean isMasterAndApprentice; //薪火结对(好像是带小弟的意思)
  private Boolean isLonelyOldMan;
  private Boolean isLifeHard;
  private Integer score;
  @NotNull(message = "退休日期不能为空")
  @ApiParam(defaultValue = "Thur Oct 30 00:00:00 CST 2021")
  private Date retireDate;
  @ApiParam(defaultValue = "Thur Oct 30 00:00:00 CST 2021")
  private Date joinPartyDate;
//  @NotNull(message = "职级不能为空")
  private Boolean isAlive; // 亡故者从通讯录删除
  private String remark; // 无法直接联系者，填家属姓名、关系、联系电话（包括微信号）

//  @ApiParam(defaultValue = "Thur Oct 30 00:00:00 CST 2021")
//  private Date moveInAt;
//  @ApiParam(defaultValue = "Thur Oct 30 00:00:00 CST 2021")
//  private Date moveOutAt;
//  private String moveOutDept;
  private String familyCondition;
  private Boolean isHistory;

  private List<VaccinationDto> vaccinationRecords;
  private List<VisitDto> visitRecords;
  private List<ActivitiesDto> activitiesRecords;
  private List<RemarkDto> remarkRecords;
  private String curUserId;

  @ApiParam(defaultValue = "Thur Oct 30 00:00:00 CST 2021")
  private Date workDate;
  private Boolean stayHere;

  private String familyMember;
  private String familyTel;
  private Boolean isPartyFeeReceived;
  private String addrType;
  private Float activeRate;

  @ApiModelProperty(hidden = true)
  public Set<Vaccination> getVaccinationEntities() {
    return convertEntities(this.vaccinationRecords, Vaccination.class);
  }

  @ApiModelProperty(hidden = true)
  public Set<Activities> getActivitiesEntities() {
    return convertEntities(this.activitiesRecords, Activities.class);
  }

  @ApiModelProperty(hidden = true)
  public Set<Visit> getVisitEntities() {
    return convertEntities(this.visitRecords, Visit.class);
  }

  @ApiModelProperty(hidden = true)
  public Set<Remark> getRemarkEntities() {
    return convertEntities(this.remarkRecords, Remark.class);
  }

  private <K,T extends BaseRelevant> Set<T> convertEntities(Collection<K> list, Class<T> c) {
    Set<T> res = new HashSet<>();
    if (list == null) {
      return res;
    }

    try {
      for (K k : list) {
        T t = c.newInstance();
        BeanUtils.copyProperties(k, t);
        t.setId(UUID.randomUUID().toString());
        t.setRetireId(this.id);
        t.setCreateAt(new Date());
        t.setCreateBy(this.curUserId);
        res.add(t);
      }
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return res;
  }
}
