package proj.modular.dto.retire;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Setter @Getter
public class ResumeDto {
  @Valid
  @NotNull(message = "开始时间不能为空")
  private Date startTime;
  private Date endTime;
  private String deptName;
}
