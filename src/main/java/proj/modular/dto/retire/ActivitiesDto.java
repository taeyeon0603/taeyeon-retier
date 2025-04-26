package proj.modular.dto.retire;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ActivitiesDto {
  private String name;
  private String addr;
  private Date eventDate;
}
