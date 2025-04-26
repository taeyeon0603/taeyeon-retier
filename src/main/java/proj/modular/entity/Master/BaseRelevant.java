package proj.modular.entity.Master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@Setter @Getter
public abstract class BaseRelevant {
  @Id
  private String id;
  @Column(name = "retire_id")
  private String retireId;
  @Column(name = "create_at")
  private Date createAt;
  @Column(name = "create_by")
  private String createBy;
}
