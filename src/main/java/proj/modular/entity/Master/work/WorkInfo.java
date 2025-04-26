package proj.modular.entity.Master.work;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "work_info", schema= "retire")
@Setter @Getter
public class WorkInfo {
  @Id
  private String id;
  private String title;
  private String remark;
  @Column(name = "is_deleted")
  private Boolean isDeleted;
  @Column(name = "create_at")
  private Date createAt;
  @Column(name = "create_by")
  private String createBy;
}
