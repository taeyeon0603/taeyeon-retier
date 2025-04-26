package proj.modular.dto.router;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class RouterDto {
  private String name;
  private String path;
  private String component;
  private String redirect;
  private MetaDto meta;
  private List<RouterDto> children;
}
