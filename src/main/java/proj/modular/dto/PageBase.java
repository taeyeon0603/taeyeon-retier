package proj.modular.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter @Getter
public class PageBase {
  @NotNull(message = "页码不能为空")
  @Min(value = 1, message = "页码不能小于1")
  private Integer page;
  @NotNull(message = "每页记录数不能为空")
  @Min(value = 1, message = "每页记录数不能小于1")
  private Integer pageSize;

  public void initPageable() {
    if (getPage() == null || getPage() < 1) {
      setPage(1);
    }
    if (getPageSize() == null || getPageSize() < 1) {
      setPageSize(10);
    }
  }
}
