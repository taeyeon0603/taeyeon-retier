package proj.modular.dto.retire;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
public class RemarkDto {
  private String content;
  private String addr;
  private Date eventDate;
}
