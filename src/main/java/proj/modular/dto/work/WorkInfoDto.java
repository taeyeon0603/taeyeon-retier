package proj.modular.dto.work;

import lombok.Getter;
import lombok.Setter;
import proj.modular.dto.FileDto;

import java.util.List;

@Setter @Getter
public class WorkInfoDto {
  private String id;
  private String title;
  private List<FileDto> files;
  private String remark;
}
