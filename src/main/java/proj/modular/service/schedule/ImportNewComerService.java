package proj.modular.service.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;
import proj.modular.dao.Master.RetireInfoRepo;
import proj.modular.entity.Master.RetireInfo;
import proj.util.Log;
import proj.util.Quartz.ICustomJob;
import proj.util.enums.RetirePoliceType;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImportNewComerService implements ICustomJob {
  private final Log logger;
  private final RetireInfoRepo retireInfoRepo;

  public ImportNewComerService(Log logger, RetireInfoRepo retireInfoRepo) {
    this.logger = logger;
    this.retireInfoRepo = retireInfoRepo;
  }

  @Override
  public void before(JobExecutionContext jobExecutionContext) {
    logger.info("新人导入任务 开始");
  }

  @Override
  public void after(JobExecutionContext jobExecutionContext) {
    logger.info("新人导入任务 结束");
  }

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    List<RetireInfo> newComerList = retireInfoRepo.findAll((e, cq, cb) ->
      cq.where(cb.and(
          cb.equal(e.get("isDel"), false),
          cb.equal(e.get("type"), RetirePoliceType.New.getCode())
      )).orderBy(cb.desc(e.get("retireDate"))).getRestriction()
    );

    List<RetireInfo> inLinePerson = new ArrayList<>();
    newComerList.stream().filter(this::isInLineWithRetire).forEach(p -> {
      p.setType(RetirePoliceType.Normal.getCode());
      inLinePerson.add(p);
    });

    if (inLinePerson.size() > 0) {
      retireInfoRepo.saveAll(inLinePerson);
    }
  }

  private boolean isInLineWithRetire(RetireInfo p) {
    return "男".equals(p.getGender()) && p.getAge() >= 65
        && "女".equals(p.getGender()) && p.getAge() >= 60;
  }
}
