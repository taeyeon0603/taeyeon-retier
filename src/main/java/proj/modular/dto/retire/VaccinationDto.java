package proj.modular.dto.retire;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
public class VaccinationDto {
  private String batchNo;
  private String inoculateObj;
  private Date inoculateAt;
  private String inoculateAddr;
  private String type;
}
