package proj.modular.vo.index;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import proj.modular.entity.Master.ResidenceType;
import proj.modular.entity.Master.RetireInfo;

import java.util.Date;

@Setter @Getter
public class DistCalcVo {
  public DistCalcVo() {
  }

  public DistCalcVo(String idCard, Date retireDate, Date workDate, Date joinPartyDate, String deptId, Integer manageDeptId, ResidenceType residenceType) {
    this.idCard = idCard;
    this.retireDate = retireDate;
    this.workDate = workDate;
    this.joinPartyDate = joinPartyDate;
    this.deptId = deptId;
    this.manageDeptId = manageDeptId;
    this.residenceType = residenceType;
  }

  private String idCard;
  private Date retireDate;
  private Date workDate;
  private Date joinPartyDate;
  private String deptId;
  private Integer manageDeptId;
  private ResidenceType residenceType;

  public RetireInfo castRetireInfo() {
    RetireInfo r = new RetireInfo();
    BeanUtils.copyProperties(this, r);
    return r;
  }
}
