package proj.modular.dto.work;

import lombok.Getter;
import lombok.Setter;
import proj.modular.dto.FileDto;

import java.util.List;

@Setter @Getter
public class WorkNewsDto {
  private String id;
  private String title;
  private String content;
  private List<FileDto> files;
}
