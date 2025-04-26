package proj.modular.vo.router;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class RouterVo {
  private String id;
  private String name;
  private String path;
  private String component;
  private String redirect;
  private MetaVo meta;
  private List<RouterVo> children;
}
