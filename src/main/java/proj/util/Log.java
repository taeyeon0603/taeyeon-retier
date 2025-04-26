package proj.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j(topic = "app-log")
@Component
public class Log {
  public void info(String msg) {
    log.info(msg + ", at " + DateUtils.localDateTime2Str(LocalDateTime.now(), "yyyy-MM-dd hh:mm:ss"));
  }

  public void error(Throwable e) {
    StackTraceElement t = e.getStackTrace()[0];
    String msg = t.getFileName() + " " + t.getLineNumber() + " " + t.getClassName() + " " + t.getMethodName();
    log.error(msg + ", at " + DateUtils.localDateTime2Str(LocalDateTime.now(), "yyyy-MM-dd hh:mm:ss"));
  }

  public void error(Throwable e, Boolean needDetail) {
    String msg;
    if (needDetail) {
      msg = Arrays.toString(e.getStackTrace());
    } else {
      StackTraceElement t = e.getStackTrace()[0];
      msg = t.getFileName() + " " + t.getLineNumber() + " " + t.getClassName() + " " + t.getMethodName();
    }
    log.error(msg + ", at " + DateUtils.localDateTime2Str(LocalDateTime.now(), "yyyy-MM-dd hh:mm:ss"));
  }
}
