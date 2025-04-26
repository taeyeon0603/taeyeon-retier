package proj.modular.entity.Master;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Setter @Getter
@Table(name = "dept")
public class Dept implements Serializable {
  @Id
  @Column(name = "dept_id")
  private String deptId;
  @Column(name = "long_dept_id")
  private String longDeptId;
  @Column(name = "dept_name")
  private String deptName;
}
