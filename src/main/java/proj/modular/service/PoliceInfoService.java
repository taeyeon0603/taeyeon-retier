package proj.modular.service;

import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import proj.modular.dao.Master.PoliceStationRepo;
import proj.modular.dao.Master.ResumeRepo;
import proj.modular.dao.Master.RetireInfoRepo;
import proj.modular.dao.Master.RewardRepo;
import proj.modular.dto.retire.*;
import proj.modular.entity.ExcelHandleRes;
import proj.modular.entity.Master.*;
import proj.modular.vo.retire.*;
import proj.util.Configuration.Constant;
import proj.util.DateUtils;
import proj.util.Log;
import proj.util.ShiroUtils;
import proj.util.enums.RetirePoliceType;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static proj.util.pubMethods.fuzzy;

@Service
public class PoliceInfoService {
  private final RetireInfoRepo retireInfoRepo;
  private final RewardRepo rewardRepo;
  private final ResumeRepo resumeRepo;
  private final PoliceStationRepo policeStationRepo;
  private final ShiroUtils shiroUtils;
  private final Constant constant;
  private final Log logger;

  public PoliceInfoService(RetireInfoRepo retireInfoRepo, RewardRepo rewardRepo, ResumeRepo resumeRepo, PoliceStationRepo policeStationRepo, ShiroUtils shiroUtils, Constant constant, Log logger) {
    this.retireInfoRepo = retireInfoRepo;
    this.rewardRepo = rewardRepo;
    this.resumeRepo = resumeRepo;
    this.policeStationRepo = policeStationRepo;
    this.shiroUtils = shiroUtils;
    this.constant = constant;
    this.logger = logger;
  }

  @Transactional
  public RetireInfo save(RetireDto dto, RetirePoliceType type) {
    // 设置当前用户 方便对应方法读取
    dto.setCurUserId(shiroUtils.getCurUserId());

    // 退休人员基本信息
    RetireInfo retireInfo = new RetireInfo();
    if (StringUtils.isEmpty(dto.getId())) {
      dto.setId(UUID.randomUUID().toString());
      BeanUtils.copyProperties(dto, retireInfo);
      retireInfo.setCreateAt(new Date());
      retireInfo.setCreateBy(shiroUtils.getCurUserId());
      retireInfo.setOrigin(1);
      retireInfo.setIsDel(false);
    } else {
      retireInfo = retireInfoRepo.findById(dto.getId()).orElse(new RetireInfo());
      BeanUtils.copyProperties(dto, retireInfo);
      retireInfo.setUpdateAt(new Date());
      retireInfo.setUpdateBy(shiroUtils.getCurUserId());
    }

    retireInfo.setType(type.getCode());
    if (dto.getPoliceStationId() == null && !StringUtils.isEmpty(dto.getPoliceStation())) {
      PoliceStation station = policeStationRepo.findByName(dto.getPoliceStation());
      if (station == null) {
        station = new PoliceStation();
        station.setName(dto.getPoliceStation());
        station = policeStationRepo.save(station);
      }
      retireInfo.setManageDeptId(station.getId());
      retireInfo.setManageDept(station.getName());
    } else if (dto.getPoliceStationId() != null) {
      retireInfo.setManageDeptId(dto.getPoliceStationId());
      retireInfo.setManageDept(dto.getPoliceStation());
    }
    retireInfo.setResidenceType(ResidenceType.findName(dto.getResidenceType()));
    retireInfo.setResidenceCommunity(dto.getCommunity());
    retireInfo.setCommunityPolice(dto.getPolice());
    retireInfo.setCommunityPoliceTel(dto.getPoliceTel());
    retireInfo.setVaccinationRecords(dto.getVaccinationEntities());
    retireInfo.setActivitiesRecords(dto.getActivitiesEntities());
    retireInfo.setVisitRecords(dto.getVisitEntities());
    retireInfo.setRemarkRecords(dto.getRemarkEntities());

    return retireInfoRepo.save(retireInfo);
  }

  public RetireInfo findById(String retireId) {
    RetireInfo retireInfo = retireInfoRepo.findById(retireId).orElse(null);
    return retireInfo != null && !retireInfo.getIsDel() ? retireInfo : null;
  }

  @Transactional
  public boolean updateReward(RetireInfo retireInfo, List<? extends RewardDto> rewardDto) {
    String retireId = retireInfo.getId();
    rewardRepo.deleteAllByRetireId(retireId);

    String optUserId = shiroUtils.getCurUserId();
    List<Reward> rewards = new LinkedList<>();
    for (RewardDto dto : rewardDto) {
      Reward reward = new Reward();
      BeanUtils.copyProperties(dto, reward);
      reward.setRetireId(retireId);
      reward.setCreateAt(new Date());
      reward.setCreateBy(optUserId);
      rewards.add(reward);
    }
    return rewardRepo.saveAll(rewards).size() > 0;
  }

  @Transactional
  public boolean updateResume(RetireInfo retireInfo, List<? extends ResumeDto> dtoList) {
    String retireId = retireInfo.getId();
    resumeRepo.deleteByRetireId(retireId);

    String optUserId = shiroUtils.getCurUserId();
    List<Resume> resumes = new LinkedList<>();
    for (ResumeDto dto : dtoList) {
      Resume resume = new Resume();
      resume.setId(UUID.randomUUID().toString());
      resume.setRetireId(retireId);
      resume.setStartTime(dto.getStartTime());
      resume.setEndTime(dto.getEndTime());
      resume.setDeptName(dto.getDeptName());
      resume.setCreateAt(new Date());
      resume.setCreateBy(optUserId);
      resumes.add(resume);
    }
    return resumeRepo.saveAll(resumes).size() > 0;
  }

  public String uploadPic(MultipartFile file) {
    //图片文件夹路径确认
    File iconPath = new File(constant.getPicPath());
    if (!iconPath.isDirectory()) {
      iconPath.mkdirs();
    }
    //文件的原始名称
    String oriFilename = file.getOriginalFilename();
    //获取文件类型
    String fileType = oriFilename.substring(oriFilename.lastIndexOf(".") + 1);
    //随机生成一个UUID
    String filename = UUID.randomUUID().toString() + "." + fileType;
    File pic = new File(iconPath.getAbsolutePath() + "/" + filename);

    try {
      file.transferTo(pic);
      return constant.getPicUrl() + filename;
    } catch (IOException e) {
      logger.error(e);
    }
    return null;
  }

  public Page<RetireVo> getPage(RetirePageDto dto, RetirePoliceType type) {
    Pageable pageable = PageRequest.of(dto.getPage() - 1, dto.getPageSize());
    Set<Dept> viewDepts = shiroUtils.getSessionAttr("user", User.class).getViewDepts();
    Set<PoliceStation> manageDepts = shiroUtils.getSessionAttr("user", User.class).getManageDepts();
    List<String> viewDeptIds = viewDepts.stream().map(Dept::getDeptId).collect(Collectors.toList());
    List<Integer> manageDeptIds = manageDepts.stream().map(PoliceStation::getId).collect(Collectors.toList());

    LocalDateTime now = LocalDateTime.now();
    String birthStart = !ObjectUtils.isEmpty(dto.getAgeEnd()) ? DateUtils.date2str(DateUtils.calcMinBirth(dto.getAgeEnd() + 1), "yyyyMMdd") : null;
    String birthEnd = !ObjectUtils.isEmpty(dto.getAgeStart()) ? DateUtils.date2str(DateUtils.calcMinBirth(dto.getAgeStart()), "yyyyMMdd") : null;
    ResidenceType residenceType = !ObjectUtils.isEmpty(dto.getResidenceType()) ? ResidenceType.findName(dto.getResidenceType()) : null;
    Date workStart = !ObjectUtils.isEmpty(dto.getPoliceAgeEnd()) ? DateUtils.localDateTime2Date(now.minusYears(dto.getPoliceAgeEnd())) : null;
    Date workEnd = !ObjectUtils.isEmpty(dto.getPoliceAgeStart()) ? DateUtils.localDateTime2Date(now.minusYears(dto.getPoliceAgeStart())) : null;
    Date joinPartyStart = !ObjectUtils.isEmpty(dto.getPartyAgeEnd()) ? DateUtils.localDateTime2Date(now.minusYears(dto.getPartyAgeEnd())) : null;
    Date joinPartyEnd = !ObjectUtils.isEmpty(dto.getPartyAgeStart()) ? DateUtils.localDateTime2Date(now.minusYears(dto.getPartyAgeStart())) : null;

    Page<RetireInfo> page = retireInfoRepo.findAll((e, cq, cb) -> cq.where(cb.and(
        cb.equal(e.get("isDel"), false)
        , cb.or(
            viewDeptIds.size() > 0 ? cb.in(e.get("deptId")).value(viewDeptIds) : cb.disjunction() // 权限可见
            , manageDeptIds.size() > 0 ? cb.in(e.get("manageDeptId")).value(manageDeptIds) : cb.disjunction() // 拥有管辖权限, 同样可见
        )
        , cb.equal(e.get("type"), type.getCode()) // 列表分类: 现有、历史、新人、编外

        , !ObjectUtils.isEmpty(dto.getPoliticalIdentity()) ? cb.equal(e.get("politicalIdentity"), dto.getPoliticalIdentity()) : cb.conjunction()
        , !ObjectUtils.isEmpty(dto.getDept()) ? cb.like(e.get("deptName"), fuzzy(dto.getDept())) : cb.conjunction()
        , !ObjectUtils.isEmpty(dto.getName()) ? cb.like(e.get("name"), fuzzy(dto.getName())) : cb.conjunction()
        , !ObjectUtils.isEmpty(dto.getIdCard()) ? cb.like(e.get("idCard"), fuzzy(dto.getIdCard())) : cb.conjunction()
        , !ObjectUtils.isEmpty(dto.getPartyBranch()) ? cb.equal(e.get("party"), dto.getPartyBranch()) : cb.conjunction()
        , !ObjectUtils.isEmpty(dto.getGender()) ? cb.equal(e.get("gender"), dto.getGender()) : cb.conjunction()
        , !ObjectUtils.isEmpty(dto.getActiveRate()) ? cb.equal(e.get("activeRate"), dto.getActiveRate()) : cb.conjunction()
        , !ObjectUtils.isEmpty(residenceType) ? cb.equal(e.get("residenceType"), residenceType) : cb.conjunction()
        // 居住地查询
        , !ObjectUtils.isEmpty(dto.getResidencePoliceStation()) ? cb.equal(e.get("manageDept"), dto.getResidencePoliceStation()) : cb.conjunction()
        // 年龄区间查询
        , !ObjectUtils.isEmpty(birthStart) ? cb.greaterThan(cb.substring(e.get("idCard"), 7, 8), birthStart) : cb.conjunction()
        , !ObjectUtils.isEmpty(birthEnd) ? cb.lessThanOrEqualTo(cb.substring(e.get("idCard"), 7, 8), birthEnd) : cb.conjunction()
        // 警龄查询
        , !ObjectUtils.isEmpty(workStart) ? cb.greaterThan(e.get("workDate"), workStart) : cb.conjunction()
        , !ObjectUtils.isEmpty(workEnd) ? cb.lessThanOrEqualTo(e.get("workDate"), workEnd) : cb.conjunction()
        // 党龄查询
        , !ObjectUtils.isEmpty(joinPartyStart) ? cb.greaterThan(e.get("joinPartyDate"), joinPartyStart) : cb.conjunction()
        , !ObjectUtils.isEmpty(joinPartyEnd) ? cb.lessThanOrEqualTo(e.get("joinPartyDate"), joinPartyEnd) : cb.conjunction()
        )).orderBy(cb.desc(e.get("retireDate"))).getRestriction(), pageable
    );

    // 补充筛选后的结果内容
    if (page.getSize() > 0) {
      List<String> retireIds = page.stream().map(RetireInfo::getId).collect(Collectors.toList());

      // 履历
      Map<String, List<Resume>> resumeMap = resumeRepo.findAllByRetireIdIn(retireIds)
          .stream().filter(Objects::nonNull)
          .sorted(Comparator.comparing(Resume::getStartTime, Comparator.nullsFirst(Comparator.naturalOrder())))
          .collect(Collectors.groupingBy(Resume::getRetireId));

      // 奖励情况
      Map<String, List<Reward>> rewardMap = rewardRepo.findAllByRetireIdIn(retireIds)
          .stream().filter(Objects::nonNull)
          .sorted(Comparator.comparing(Reward::getRewardDate, Comparator.nullsFirst(Comparator.naturalOrder())))
          .collect(Collectors.groupingBy(Reward::getRetireId));

      return page.map(r -> {
        RetireVo vo = new RetireVo();
        BeanUtils.copyProperties(r, vo);

        vo.setPoliceStation(r.getManageDept());
        vo.setCommunity(r.getResidenceCommunity());
        vo.setPolice(r.getCommunityPolice());
        vo.setPoliceTel(r.getCommunityPoliceTel());
        if (r.getResidenceType() != null) {
          vo.setResidenceType(r.getResidenceType().getLabel());
        }

        vo.setResumeList(convertObjList(resumeMap.get(r.getId()), ResumeVo.class));
        vo.setRewardList(convertObjList(rewardMap.get(r.getId()), RewardVo.class));
        vo.setVaccinationList(convertObjList(r.getVaccinationRecords(), VaccinationVo.class));
        vo.setActivitiesList(convertObjList(r.getActivitiesRecords(), ActivitiesVo.class));
        vo.setVisitList(convertObjList(r.getVisitRecords(), VisitVo.class));
        vo.setRemarkList(convertObjList(r.getRemarkRecords(), RemarkVo.class));
        vo.setIsHistory(r.getIsHistory());
        if (r.getActiveRate() == null) {
          vo.calculateActiveRate();
        }
        return vo;
      });
    }
    return new PageImpl<>(new ArrayList<>(0), pageable, 0);
  }

  private <T, K> List<K> convertObjList(Collection<T> collection, Class<K> clazz) {
    if (collection == null || clazz == null) {
      return new ArrayList<>(0);
    }

    List<K> res = new LinkedList<>();
    try {
      for (T t : collection) {
        K k = clazz.newInstance();
        BeanUtils.copyProperties(t, k);
        handleVoIfNull(k);
        res.add(k);
      }
    } catch (InstantiationException | IllegalAccessException e) {
      e.printStackTrace();
    }
    return res;
  }

  public boolean deleteRetireInfo(RetireInfo retireInfo) {
    retireInfo.setIsDel(true);
    return retireInfoRepo.save(retireInfo) != null;
  }

  public List<RetireExportVo> getAll(RetireSearchDto dto, RetirePoliceType type) {
    Set<Dept> viewDepts = shiroUtils.getSessionAttr("user", User.class).getViewDepts();
    Set<PoliceStation> manageDepts = shiroUtils.getSessionAttr("user", User.class).getManageDepts();
    List<String> viewDeptIds = viewDepts.stream().map(Dept::getDeptId).collect(Collectors.toList());
    List<Integer> manageDeptIds = manageDepts.stream().map(PoliceStation::getId).collect(Collectors.toList());

    List<RetireInfo> list = retireInfoRepo.findAll((e, cq, cb) ->
        cq.where(cb.and(
            cb.equal(e.get("isDel"), false),
            cb.or(
                viewDeptIds.size() > 0 ? cb.in(e.get("deptId")).value(viewDeptIds) : cb.disjunction() // 权限可见
                , manageDeptIds.size() > 0 ? cb.in(e.get("manageDeptId")).value(manageDeptIds) : cb.disjunction() // 拥有管辖权限, 同样可见
            ),
            cb.like(e.get("deptName"), fuzzy(dto.getDept())),
            cb.like(e.get("name"), fuzzy(dto.getName())),
            cb.like(e.get("idCard"), fuzzy(dto.getIdCard())),
            cb.equal(e.get("type"), type.getCode())
        )).orderBy(cb.desc(e.get("retireDate"))).getRestriction());

    // 履历
    Map<String, List<Resume>> resumeMap = resumeRepo.findAll().stream()
        .filter(Objects::nonNull).collect(Collectors.groupingBy(Resume::getRetireId));
    // 奖励情况
    Map<String, List<Reward>> rewardMap = rewardRepo.findAll().stream()
        .filter(Objects::nonNull).collect(Collectors.groupingBy(Reward::getRetireId));

    return list.stream().map(r -> {
      RetireExportVo v = new RetireExportVo();
      BeanUtils.copyProperties(r, v);

      v.setPoliceStation(r.getManageDept());
      v.setCommunity(r.getResidenceCommunity());
      v.setPolice(r.getCommunityPolice());
      v.setPoliceTel(r.getCommunityPoliceTel());

      v.setIsAlive(r.getIsAlive() ? "是" : "否");
      v.setIsHistory(r.getIsHistory() ? "是" : "否");
      v.setIsLifeHard(r.getIsLifeHard() ? "是" : "否");
      v.setIsLonelyOldMan(r.getIsLonelyOldMan() ? "是" : "否");

      v.setResumeRecords(convertStr(convertObjList(resumeMap.get(r.getId()), ResumeVo.class)));
      v.setRewardRecords(convertStr(convertObjList(rewardMap.get(r.getId()), RewardVo.class)));
      v.setActivitiesRecords(convertStr(convertObjList(r.getActivitiesRecords(), ActivitiesVo.class)));
      v.setVisitRecords(convertStr(convertObjList(r.getVisitRecords(), VisitVo.class)));
      v.setRemarkRecords(convertStr(convertObjList(r.getRemarkRecords(), RemarkVo.class)));
      v.setVaccinationRecords(convertStr(convertObjList(r.getVaccinationRecords(), VaccinationVo.class)));

      handleVoIfNull(v);
      return v;
    }).collect(Collectors.toList());
  }

  private <T> void handleVoIfNull(T vo) {
    Field[] fields = vo.getClass().getDeclaredFields();
    for (int i = 0; i < fields.length; i++) {
      fields[i].setAccessible(true);
      String name = fields[i].getName();
      String type = fields[i].getGenericType().toString();
      name = name.replaceFirst(name.substring(0, 1), name.substring(0, 1).toUpperCase());

      if (type.equals("class java.lang.String")) {
        try {
          Method m = vo.getClass().getMethod("get" + name);
          String value = (String) m.invoke(vo);
          if (StringUtils.isEmpty(value)) {
            fields[i].set(vo, fields[i].getType().getConstructor(fields[i].getType()).newInstance("无记录"));
          }
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private <T> String convertStr(List<T> list) {
    if (list == null || list.size() == 0) {
      return "";
    }
    return list.stream().map(Object::toString).collect(Collectors.joining("\n"));
  }

  public void importExcelHandleData(ExcelHandleRes res) {
    List<RetireInfo> retires = retireInfoRepo.findAllByIsDelIsFalseAndTypeEquals(0);
    List<String> retireIdCards = retires.stream().map(RetireInfo::getIdCard).collect(Collectors.toList());
    Map<String, RetireInfo> idCardRetireRef = retires.stream().collect(Collectors.toMap(RetireInfo::getIdCard, r -> r));

    for (RetireDto dto : res.getRetireList().stream().filter(r -> !StringUtils.isEmpty(r.getIdCard())).collect(Collectors.toList())) {
      if (!retireIdCards.contains(dto.getIdCard())) {
        dto.setIsAlive(true);
        RetireInfo r = save(dto, RetirePoliceType.Normal);
        idCardRetireRef.putIfAbsent(r.getIdCard(), r);
      } else {
        // 对已存在的退休民警，进行部分数据的更新
        RetireInfo r = retireInfoRepo.findByIdCard(dto.getIdCard());
        r.setDeptId(dto.getDeptId());
        r.setDeptName(dto.getDeptName());
        r.setTel(dto.getTel());
//        r.setParty(dto.getParty());
        r.setRetireDate(dto.getRetireDate());
        r.setJoinPartyDate(dto.getJoinPartyDate());
        r.setPoliticalIdentity(dto.getPoliticalIdentity());
        r.setPost(dto.getPost());
        r.setLevel(dto.getLevel());
        r.setHealthStatus(dto.getHealthStatus());
        r.setFamilyCondition(dto.getFamilyCondition());
        r.setResidenceCommunity(dto.getCommunity());
        r.setCommunityPolice(dto.getPolice());
        r.setDeptId(dto.getDeptId());
        r.setDeptName(dto.getDeptName());
        retireInfoRepo.save(r);
      }
    }

    if (res.getResumes().size() > 0) {
      res.getResumes().stream().collect(Collectors.groupingBy(ResumeImportDto::getIdCard)).forEach((idCard, resumes) -> {
        RetireInfo retireInfo = idCardRetireRef.get(idCard);
        if (retireInfo != null) {
          updateResume(retireInfo, resumes);
        }
      });
    }

    if (res.getRewards().size() > 0) {
      res.getRewards().stream().collect(Collectors.groupingBy(RewardImportDto::getIdCard)).forEach((idCard, rewards) -> {
        RetireInfo retireInfo = idCardRetireRef.get(idCard);
        if (retireInfo != null) {
          updateReward(retireInfo, rewards);
        }
      });
    }
  }
}
