package proj.util.Annotation;

import proj.util.Annotation.Processor.OptType;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 无法独立使用
 * 该注解依赖于当前api服务的部分函数
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface UploadLog { //上传日志
  OptType optType() default OptType.login; //OptObj的可读取属性
  String module() default ""; //OptObj的可读取属性

  @Target({FIELD, PARAMETER})
  @Retention(RUNTIME)
  @Documented
  @interface optCondition { //使用该注解的参数必须可转String类或完善其toString()方法
    String value() default "";
  }
}
