package proj.modular.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
public class IndexVisitRecord {
  private String visit;
  private String policeStation;
  private String visitBy;
  private Date visitDate;
}
