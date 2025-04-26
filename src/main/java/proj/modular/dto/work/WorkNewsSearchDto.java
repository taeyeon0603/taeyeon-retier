package proj.modular.dto.work;

import lombok.Getter;
import lombok.Setter;
import proj.modular.dto.PageBase;

@Setter @Getter
public class WorkNewsSearchDto extends PageBase {
  private String title;
  private String content;
}
