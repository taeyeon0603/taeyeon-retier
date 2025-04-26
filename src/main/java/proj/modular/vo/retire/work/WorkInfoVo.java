package proj.modular.vo.retire.work;

import lombok.Getter;
import lombok.Setter;
import proj.modular.vo.FileVo;

import java.util.Date;
import java.util.List;

@Setter @Getter
public class WorkInfoVo {
  private String id;
  private String title;
  private List<FileVo> files;
  private String remark;
  private Date createAt;
  private String createBy;
}
