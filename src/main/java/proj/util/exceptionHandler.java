package proj.util;

import org.apache.shiro.authz.AuthorizationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import proj.util.enums.errors;

import java.util.List;

@RestControllerAdvice
public class exceptionHandler {
  /**
   * 参数验证
   *
   * @param e
   * @return
   */
  @ExceptionHandler(value = MethodArgumentNotValidException.class)
  Tip BindingResult(MethodArgumentNotValidException e) {
    BindingResult res = e.getBindingResult();
    if (res.hasErrors()) {
      List<FieldError> errors = res.getFieldErrors();
      if (errors.size() > 0) {
        return Tip.fail(-2, errors.get(0).getDefaultMessage());
      }
    }
    return Tip.fail(errors.ParamsValidateFailed);
  }

  @ExceptionHandler(AuthorizationException.class)
  Tip throwUnauthorizedException(Exception e) {
    return Tip.fail(403, "当前用户无操作权限");
  }
}
