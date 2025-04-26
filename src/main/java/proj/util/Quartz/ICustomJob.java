package proj.util.Quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;

public interface ICustomJob extends Job {
  void before(JobExecutionContext jobExecutionContext);

  void after(JobExecutionContext jobExecutionContext);
}
