package proj.util;

import lombok.Getter;
import lombok.Setter;
import proj.util.enums.errors;

/**
 * 返回给前台的提示（最终转化为json形式）
 *
 * @author fengshuonan
 * @Date 2017年1月11日 下午11:58:00
 */
@Getter @Setter
public class Tip<T> {
  private int code;
  private String message;
  private T data;

  public Tip(int code, String message, T data) {
    this.code = code;
    this.message = message;
    this.data = data;
  }

  public static <T> Tip<T> ok() {
    return new Tip<>(200, "true", null);
  }

  public static <T> Tip<T> ok(T data) {
    return new Tip<>(200, "true", data);
  }

  public static <T> Tip<T> fail(int code, String msg) {
    return new Tip<>(500, msg, null);
  }

  public static <T> Tip<T> fail(errors e) {
    return new Tip<>(e.getCode(), e.getMsg(), null);
  }
}
