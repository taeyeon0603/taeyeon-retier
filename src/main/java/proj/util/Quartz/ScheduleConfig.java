package proj.util.Quartz;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import proj.modular.service.schedule.ImportNewComerService;

@Configuration
public class ScheduleConfig {
  @Bean
  public JobDetail importNewComer() {
    return JobBuilder.newJob(ImportNewComerService.class)
        .withIdentity("新人导入")
        .storeDurably(true)
        .requestRecovery()
        .build();
  }

  @Bean
  public Trigger importNewComerTrigger() {
    return TriggerBuilder.newTrigger()
        .forJob(importNewComer())
        .withIdentity("新人导入", "retire")
        .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0 1 * ?"))
        .build();
  }
}
