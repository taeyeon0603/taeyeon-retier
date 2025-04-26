package proj.modular.vo.index;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class AgeDistVo {
  public AgeDistVo() {
    a50to59 = 0;
    a60to69 = 0;
    a70to79 = 0;
    a80to89 = 0;
    a90to99 = 0;
    over100 = 0;
  }

  private Integer a50to59;
  private Integer a60to69;
  private Integer a70to79;
  private Integer a80to89;
  private Integer a90to99;
  private Integer over100;
}
