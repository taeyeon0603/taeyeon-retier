package proj.modular.entity.Master;

import lombok.Getter;
import lombok.Setter;
import proj.util.Annotation.Processor.OptType;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@Table(name = "sys_log")
public class Log {
  @Id
  @Column(columnDefinition = "varchar(36)")
  private String id;
  @Column(name = "user_id", columnDefinition = "varchar(36)")
  private String userId;
  @Column(name = "name", columnDefinition = "varchar(64) comment '操作人姓名'")
  private String userName;
  @Column(name = "dept_id", columnDefinition = "varchar(36)")
  private String deptId;
  @Column(name = "dept_name", columnDefinition = "varchar(64) comment '操作人所属部门'")
  private String deptName;
  @Column(name = "terminal_ip", columnDefinition = "varchar(40) comment '终端标识(网络IP)'")
  private String terminalIP;
  @Enumerated
  @Column(name = "opt_type", columnDefinition = "int(1) comment '操作类型代表，0：登录；1：查询；2：新增；3：修改；4：删除'")
  private OptType optType;
  @Column(name = "is_success", columnDefinition = "bit(1)")
  private Boolean isSuccess;
  @Column(name = "opt_module", columnDefinition = "varchar(64) comment '功能名称'")
  private String optModule;
  @Column(name = "opt_condition", columnDefinition = "varchar(3000) comment '操作条件(登录时置空)'")
  private String optCondition;
  @Column(name = "opt_time")
  private Date optTime;
}
