package proj.modular.entity.Master;

import com.alibaba.druid.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import proj.util.DateUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "retire_info", schema= "retire")
@Setter @Getter
public class RetireInfo {
  @Id
  @Column(length = 36)
  private String id;
  @Column(length = 20)
  private String name;
  @Column(name = "id_card", length = 32)
  private String idCard;
  @Column(length = 10)
  private String gender;
  @Column(length = 20)
  private String tel;
  @Column(name = "short_tel", length = 6)
  private String shortTel;
  @Column(name = "dept_id", length = 36)
  private String deptId;
  @Column(name = "dept_name", length = 40)
  private String deptName;
  @Column(length = 20)
  private String post;
  @Column(length = 20)
  private String level;
  @Column(columnDefinition = "text comment '照片base64'")
  private String pic;
  @Column(length = 10)
  private String party;
//  @Column(name = "residence_ref_id")
//  private Integer residenceRefId;
  @Column(name = "manage_dept", length = 50)
  private String manageDept;
  @Column(name = "manage_dept_id", length = 36)
  private Integer manageDeptId;
  @Column(name = "residence_community", length = 70)
  private String residenceCommunity;
  @Column(name = "community_police", length = 20)
  private String communityPolice;
  @Column(name = "community_police_tel", length = 20)
  private String communityPoliceTel;
  @Column(name = "residence_addr", length = 100)
  private String residenceAddr;
  @Enumerated
  @Column(name = "residence_type")
  private ResidenceType residenceType;
  @Column(name = "health_status", length = 10)
  private String healthStatus; // 字典表
  @Column(length = 20)
  private String disease;
  @Column(length = 10)
  private String activities;
  @Column(name = "fixed_tel", length = 20)
  private String fixedTel;
  @Column(name = "political_identity", length = 20)
  private String politicalIdentity;
  @Column(name = "pension_bank_card", length = 30, columnDefinition = "comment '退休金银行卡号' ")
  private String pensionBankCard;
  @Column(name = "is_alive")
  private Boolean isAlive;
  private String remark;
  @Column(name = "join_party_date")
  private Date joinPartyDate;
  @Column(name = "retire_date")
  private Date retireDate;
  private Integer origin;
  @Column(name = "is_master_and_apprentice")
  private Boolean isMasterAndApprentice;
  @Column(name = "is_lonely_old_man")
  private Boolean isLonelyOldMan;
  @Column(name = "is_life_hard")
  private Boolean isLifeHard;
  @Column(name = "is_del")
  private Boolean isDel;
  @Column(name = "create_at")
  private Date createAt;
  @Column(name = "update_at")
  private Date updateAt;
  @Column(name = "create_by", length = 36)
  private String createBy;
  @Column(name = "update_by", length = 36)
  private String updateBy;

  @Column(name = "move_in_at")
  private Date moveInAt;
  @Column(name = "move_out_at")
  private Date moveOutAt;
  @Column(name = "move_out_dept", length = 40)
  private String moveOutDept;
  @Column(name = "family_condition", length = 50)
  private String familyCondition;
  private Integer type; // 0:现有 1:历史

  @Column(name = "work_date")
  private Date workDate;
  @Column(name = "stay_here")
  private Boolean stayHere;

  @Column(name = "family_member", columnDefinition = "comment '家庭成员'")
  private String familyMember;
  @Column(name = "family_tel", length = 100, columnDefinition = "comment '家属联系方式'")
  private String familyTel;
  @Column(name = "is_party_fee_received", columnDefinition = "bit(1) comment '党费是否收缴'")
  private Boolean isPartyFeeReceived;
  @Column(name = "addr_type", length = 20, columnDefinition = "comment '居住地类型'")
  private String addrType;
  @Column(name = "active_rate", columnDefinition = "comment '活跃度'")
  private Float activeRate;

  @Column(columnDefinition = "comment '分数'")
  private Integer score;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "retire_id")
  private Set<Vaccination> vaccinationRecords;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "retire_id")
  private Set<Activities> activitiesRecords;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "retire_id")
  private Set<Visit> visitRecords;

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  @JoinColumn(name = "retire_id")
  private Set<Remark> remarkRecords;

  // 用于vo复制birth属性
  public Date getBirth() {
    if (!StringUtils.isEmpty(idCard) && idCard.length() == 18) {
      String birth = idCard.substring(6,14);
      return DateUtils.str2Date(birth, "yyyyMMdd");
    }
    return null;
  }

  public Integer getAge() {
    Date birth = getBirth();
    if (birth != null) {
      return DateUtils.calcAge(birth);
    }
    return null;
  }

  public Integer getPartyAge() {
    if (joinPartyDate == null) {
      return 0;
    }
    return Period.between(
        joinPartyDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
        LocalDate.now()
    ).getYears();
  }

  public Integer getPoliceAge() {
    if (retireDate == null || workDate == null) {
      return null;
    }
    return (int) ((retireDate.getTime() - workDate.getTime())/(1000 * 60 * 60 * 24)/365);
  }

  public Boolean getIsHistory() {
    return type == 1;
  }
}
