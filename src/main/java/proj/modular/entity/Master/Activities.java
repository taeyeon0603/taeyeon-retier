package proj.modular.entity.Master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "activities", schema= "retire")
@Setter @Getter
public class Activities extends BaseRelevant {
  private String name;
  private String addr;
  @Column(name = "event_date")
  private Date eventDate;

  @ManyToOne
  @JoinColumn(name = "retire_id", insertable = false, updatable = false)
  private RetireInfo retireInfo;
}
