package proj.util.Annotation.Processor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import proj.modular.service.LogService;
import proj.util.Annotation.UploadLog;
import proj.util.Tip;

import java.lang.annotation.Annotation;

@Slf4j(topic = "app-log")
@Aspect
@Component
public class UploadLogProc {
  @Autowired
  private LogService logService;

  @Pointcut("@annotation(proj.util.Annotation.UploadLog)")
  public void annotationPointCut() { }

  @AfterReturning(value = "annotationPointCut()", returning = "result")
  public void afterReturning(JoinPoint joinPoint, Object result) {
    UploadLog uploadLog = ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(UploadLog.class);
    Object[] args = joinPoint.getArgs();
    Annotation[][] annotationArrays = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameterAnnotations();

    // 封装请求内容与结果
    proj.modular.entity.Master.Log log = new proj.modular.entity.Master.Log();
    log.setOptType(uploadLog.optType()); //改成枚举类型
    log.setOptModule(uploadLog.module());
    log.setOptCondition(getOptCondition(args, annotationArrays));
    if (result == null || result instanceof Tip && ((Tip) result).getCode() == 200) {
      //依赖返回结果为util.tips.Tip类，其code为200为成功
      log.setIsSuccess(true);
    } else {
      log.setIsSuccess(false);
    }

    logService.save(log);
  }

  /**
   * 获取OperationCondition
   * 通过 @optCondition 解析接口参数
   * ps: 只获取一个
   *
   * @return
   */
  private String getOptCondition(Object[] args, Annotation[][] annotationArrs) {
    for (int i = 0; i < annotationArrs.length; i++) {
      for (Annotation annotation : annotationArrs[i]) {
        //处理对象型参数(需要get方法)
        if (annotation.annotationType().equals(RequestBody.class)) {
          return JSON.toJSONString(args[0]);
        } else if (annotation.annotationType().equals(UploadLog.optCondition.class)) {
          return String.valueOf(args[i]); //需要是可转String类
        }
      }
    }

    // 非json类请求，直接返回query
    RequestAttributes attr = RequestContextHolder.getRequestAttributes();
    ServletRequestAttributes servletReq = (ServletRequestAttributes) attr;
    if (servletReq != null) {
      JSONObject params = new JSONObject();
      servletReq.getRequest().getParameterMap().forEach((key,values) -> {
        if (values.length == 1) {
          params.put(key, values[0]);
        } else {
          params.put(key, values);
        }
      });
      return params.toJSONString();
    }
    return "";
  }
}
