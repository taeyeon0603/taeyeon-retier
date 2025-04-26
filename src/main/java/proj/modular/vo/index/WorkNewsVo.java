package proj.modular.vo.index;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
public class WorkNewsVo {
  private String imgUrl;
  private String title;
  private String content;
  private Date createAt;
}
