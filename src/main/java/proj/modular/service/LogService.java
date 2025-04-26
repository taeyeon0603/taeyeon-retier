package proj.modular.service;

import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import proj.modular.dao.Master.DeptRepo;
import proj.modular.dao.Master.LogRepo;
import proj.modular.dao.Master.UserRepo;
import proj.modular.dto.LogSearchDto;
import proj.modular.entity.Master.Log;
import proj.modular.entity.Master.User;
import proj.modular.vo.SimpleLogVo;
import proj.util.Annotation.Processor.OptType;
import proj.util.ShiroUtils;
import proj.util.pubMethods;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class LogService {
  private final LogRepo logRepo;
  private final ShiroUtils shiroUtils;
  private final UserRepo userRepo;
  private final DeptRepo deptRepo;

  public LogService(LogRepo logRepo, ShiroUtils shiroUtils, UserRepo userRepo, DeptRepo deptRepo) {
    this.logRepo = logRepo;
    this.shiroUtils = shiroUtils;
    this.userRepo = userRepo;
    this.deptRepo = deptRepo;
  }

  /**
   * 封装日志
   * @param log
   * @return
   */
  public void save(Log log) {
    Optional.ofNullable(shiroUtils.getCurUserId()).ifPresent(userId -> {
      User optUser = userRepo.findById(userId).orElse(User.temp());

      if (optUser.getDeptId() != null) {
        deptRepo.findById(optUser.getDeptId()).ifPresent(dept -> {
          log.setDeptId(dept.getDeptId());
          log.setDeptName(dept.getDeptName());
        });
      }

      log.setId(UUID.randomUUID().toString());
      log.setUserId(optUser.getId());
      log.setUserName(optUser.getName());
      log.setOptTime(new Date());
      log.setTerminalIP(pubMethods.getRealIPAddr());

      logRepo.save(log);
    });
  }

  public Page<SimpleLogVo> getList(LogSearchDto dto) {
    return logRepo.findAll((e, cq, cb) -> cb.and(
        !StringUtils.isEmpty(dto.getOptUserName()) ? cb.like(e.get("userName"), "%" + dto.getOptUserName() + "%") : cb.conjunction(),
        !StringUtils.isEmpty(dto.getDeptId()) ? cb.equal(e.get("deptId"), dto.getDeptId()) : cb.conjunction(),
        dto.getStart() != null ? cb.greaterThanOrEqualTo(e.get("optTime"), dto.getStart()) : cb.conjunction(),
        dto.getEnd() != null ? cb.lessThan(e.get("optTime"), dto.getEnd()) : cb.conjunction()
    ), PageRequest.of(dto.getPage()-1, dto.getPageSize())).map(log -> {
      SimpleLogVo vo = new SimpleLogVo();
      BeanUtils.copyProperties(log, vo);

      if (log.getOptType() == OptType.login) {
        vo.setContent(log.getDeptName() + log.getUserName() + "登录了平台");
      } else {
        vo.setContent(log.getDeptName() + log.getUserName() + "进行了" + log.getOptModule() + log.getOptType().getContent());
      }
      return vo;
    });
  }
}
