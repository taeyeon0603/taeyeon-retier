package proj.modular.vo.retire;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class FamilyMemberVo {
  private Integer id;
  private String name;
  private String relation;
  private String tel;
  private String wechat;

  @Override
  public String toString() {
    return
      "姓名:" + name +
      ", 关系:" + relation +
      ", 手机号:" + tel +
      ", 微信号:" + wechat +
        ";\r\n";
  }
}
