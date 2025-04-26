package proj.modular.entity.Master;

import lombok.Getter;
import lombok.Setter;
import proj.util.DateUtils;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "reward", schema= "retire")
@Setter @Getter
public class Reward {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "retire_id")
  private String retireId;
  private String dept;
  @Column(name = "code_number")
  private String codeNumber;
  @Column(name = "reward_type")
  private String rewardType;
  private String types; //集体 or 个人
  @Column(name = "link_address")
  private String linkAddress;
  @Column(name = "pzjg")
  private String approveAddress;
  @Column(name = "pzjgjb")
  private String approveLevel;
  @Column(name = "reward_time")
  private Date rewardDate;
  @Column(name = "create_time")
  private Date createAt;
  @Column(name = "create_by")
  private String createBy;

  public Date getRewardDate() {
    return rewardDate != null? rewardDate: DateUtils.localDate2Date(LocalDate.of(1900, 1, 1));
  }
}
