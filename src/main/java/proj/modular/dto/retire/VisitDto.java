package proj.modular.dto.retire;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
public class VisitDto {
  private String content;
  private String visitDept;
  private String addr;
  private Date visitDate;
}
