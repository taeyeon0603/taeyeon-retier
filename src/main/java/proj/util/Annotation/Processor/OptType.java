package proj.util.Annotation.Processor;

import lombok.Getter;

@Getter
public enum OptType {
  login(0, "登录"), query(1, "查询"), insert(2, "新增"), update(3, "修改"),
  delete(4, "删除"), ;

  private Integer tag;
  private String content;

  OptType(Integer tag, String content) {
    this.tag = tag;
    this.content = content;
  }
}
