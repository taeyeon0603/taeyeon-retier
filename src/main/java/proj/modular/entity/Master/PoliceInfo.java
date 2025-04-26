package proj.modular.entity.Master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "new_police_info", schema= "dbo")
@Getter @Setter
public class PoliceInfo {
  @Id
  @Column(name = "id_card")
  private String idCard;
  private Integer no;
  private String code;
  private String name;
  private String gender;
  private String hometown;
  private Date birth;
  private Integer age;
  @Column(name = "work_continue_date")
  private Date workContinueDate;
  @Column(name = "join_police_date")
  private Date joinPoliceDate;
  @Column(name = "political_status")
  private String politicalStatus;
  @Column(name = "join_party_date")
  private Date joinPartyDate;
  private String dept;
  private String post;
  private String rank;
  @Column(name = "stay_Rank_date")
  private Date stayRankDate;
  private String level;
  @Column(name = "stay_level_date")
  private Date stayLevelDate;
  @Column(name = "police_code")
  private String policeCode;
  @Column(name = "edu_allday")
  private String eduAllday;
  @Column(name = "edu_inwork")
  private String eduInwork;
  @Column(name = "from_gacollage")
  private String fromGacollage;
  private String edu;
  private String tag;
}
