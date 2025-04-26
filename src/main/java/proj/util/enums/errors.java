package proj.util.enums;

import lombok.Getter;

@Getter
public enum errors {
  INSERTERROR(500, "数据新增失败"),
  UPDATEERROR(500, "数据修改失败"),
  DELETEERROR(500, "数据删除失败"),
  PARAMEMPTY(500, "参数不能为空"),
  LOGINERROR(500, "登陆失败"),
  LOGINEXPIRED(500, "登录过期"),
  ParamsValidateFailed(500, "参数错误"),
  IDEMPTY(400, "ID不能为空"),
  DateRangeError(500, "时间范围参数错误");

  private Integer code;
  private String msg;

  errors(Integer code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public static errors findByCode(Integer code) {
    for (errors error : errors.values()) {
      if (error.getCode().equals(code)) {
        return error;
      }
    }
    return null;
  }
}
