package proj.modular.entity.Master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "resume", schema= "retire")
@Getter @Setter
public class Resume {
  @Id
  private String id;
  @Column(name = "retire_id")
  private String retireId;
  @Column(name = "start_time")
  private Date startTime;
  @Column(name = "end_time")
  private Date endTime;
  @Column(name = "dept_name")
  private String deptName;
  @Column(name = "create_at")
  private Date createAt;
  @Column(name = "create_by")
  private String createBy;
}
