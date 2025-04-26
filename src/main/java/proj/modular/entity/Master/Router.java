package proj.modular.entity.Master;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "router")
@Setter @Getter
public class Router {
  @Id
  private String id;
  @Column(name = "parent_id")
  private String parentId;
  private String name;
  private String path;
  private String component;
  private String redirect;
  private String title;
  private String icon;
  @Column(name = "create_at")
  private Date createAt;
  @Column(name = "create_by")
  private String createBy;
  private Integer sort;
}
