package proj.modular.entity.Master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "remarks", schema= "retire")
@Setter @Getter
public class Remark extends BaseRelevant {
  private String addr;
  private String content;
  @Column(name = "event_date")
  private Date eventDate;

  @ManyToOne
  @JoinColumn(name = "retire_id", insertable = false, updatable = false)
  private RetireInfo retireInfo;
}
