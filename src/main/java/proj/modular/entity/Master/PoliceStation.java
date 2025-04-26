package proj.modular.entity.Master;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Setter @Getter
@Table(name = "police_station")
public class PoliceStation implements Serializable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private String name;
  @Column(name = "area_code")
  private String areaCode;
}
