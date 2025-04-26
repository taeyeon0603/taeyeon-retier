package proj.modular.entity.Master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "vaccination", schema= "retire")
@Setter @Getter
public class Vaccination extends BaseRelevant {
  @Column(name = "batch_no")
  private String batchNo;
  @Column(name = "inoculate_obj")
  private String inoculateObj;
  @Column(name = "inoculate_at")
  private Date inoculateAt;
  @Column(name = "inoculate_addr")
  private String inoculateAddr;
  private String type;

  @ManyToOne
  @JoinColumn(name = "retire_id", insertable = false, updatable = false)
  private RetireInfo retireInfo;
}
