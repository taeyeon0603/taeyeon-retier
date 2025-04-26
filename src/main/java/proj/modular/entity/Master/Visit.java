package proj.modular.entity.Master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "visit_record", schema= "retire")
@Setter @Getter
public class Visit extends BaseRelevant {
  @Column(name = "visit_dept")
  private String visitDept;
  private String addr;
  private String content;
  @Column(name = "visit_date")
  private Date visitDate;

  @ManyToOne
  @JoinColumn(name = "retire_id", insertable = false, updatable = false)
  private RetireInfo retireInfo;
}
