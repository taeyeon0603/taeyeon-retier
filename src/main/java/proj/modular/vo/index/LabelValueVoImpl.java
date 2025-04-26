package proj.modular.vo.index;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter @Getter
public class LabelValueVoImpl implements LabelValueVo {
  private String name;
  private Object value;
}
