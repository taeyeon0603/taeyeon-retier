package proj.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import proj.util.Configuration.Constant;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Aspect
@Component
public class AppAspect {
  @Autowired
  private Constant constant;
  @Autowired
  private Log logger;
  @Autowired
  private ShiroUtils shiroUtils;

  @Pointcut("execution(public * proj.modular.controller..*(..))")
  public void controllerPoint() {
  }

  @Pointcut("execution(public * proj.modular.service..*(..))")
  public void servicePoint() {
  }

  @Around(value = "controllerPoint()")
  public Object beforeApi(ProceedingJoinPoint pjp) throws Throwable {
    String p255 = "(2(5[0-5]|[0-4]\\d)|1(\\d{2})|([1-9]?[0-9]))"; //0-255正则匹配
    String remoteIP = pubMethods.getRealIPAddr(); //ps: 使用其他地址代理也会被拦截
    List<String> allowIps = Arrays.asList(constant.getAllowIp().split(","));
    StringBuilder patternStr = new StringBuilder("^");

    allowIps.forEach(ip -> {
      if (ip.indexOf('*') == -1) {
        patternStr.append("(").append(ip).append(")|");
      } else {
        patternStr.append("(")
          .append(ip, 0, ip.indexOf('*'))
          .append(p255)
          .append(")|");
      }
    });

    patternStr.deleteCharAt(patternStr.length() - 1);
    Pattern pattern = Pattern.compile(patternStr.toString());

    //确认IP在允许网段内
    if (remoteIP == null || !pattern.matcher(remoteIP).find()) {
      logger.info("IP " + remoteIP + " 位于不允许访问的网段");
      return null;
    }

    return pjp.proceed();
  }

  @AfterThrowing(value = "AppAspect.controllerPoint()", throwing = "e")
  public void afterControllerThrowing(Throwable e) {
    if (constant.getProfile().equals("test") || constant.getProfile().equals("dev")) {
      e.printStackTrace();
    } else {
      logger.info("controller-error: " + Arrays.toString(e.getStackTrace()));
    }
  }

  @AfterThrowing(value = "AppAspect.servicePoint()", throwing = "e")
  public void afterServiceThrowing(Throwable e) {
    if (constant.getProfile().equals("test") || constant.getProfile().equals("dev")) {
      e.printStackTrace();
    } else {
      logger.info("service-error: " + Arrays.toString(e.getStackTrace()));
    }
  }
}
