package proj.modular.entity.Master.work;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "work_file", schema= "retire")
@Setter @Getter
public class WorkFile {
  @Id
  private String id;
  private String name;
  private String path;
  @Column(name = "ref_id")
  private String refId;
}
