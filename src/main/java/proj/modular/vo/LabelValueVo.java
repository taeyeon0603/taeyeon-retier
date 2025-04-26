package proj.modular.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class LabelValueVo {
  private String label;
  private Object value;
  private List<LabelValueVo> children;
}
