package proj.modular.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class CascadeVo<T> {
  private Integer id;
  private String value;
  private Object label;
  private List<T> children;
}
