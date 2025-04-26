package proj.util.enums;

import lombok.Getter;

@Getter
public enum RetirePoliceType {
  Normal(0, "普通"),
  History(1, "归档"),
  New(2, "新人"),
  Extra(3, "编外"),;

  private Integer code;
  private String name;

  RetirePoliceType(Integer code, String name) {
    this.code = code;
    this.name = name;
  }
}
