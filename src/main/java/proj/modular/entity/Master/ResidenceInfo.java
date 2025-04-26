package proj.modular.entity.Master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "residence_info", schema= "retire")
@Setter @Getter
public class ResidenceInfo {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "police_station")
  private String policeStation;
  private String community;
  private String police;
  @Column(name = "police_tel")
  private String policeTel;
}
