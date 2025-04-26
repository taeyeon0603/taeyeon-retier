package proj.modular.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import proj.modular.dao.Master.RetireInfoRepo;
import proj.modular.dao.Master.VisitRepo;
import proj.modular.dao.Master.work.WorkNewsRepo;
import proj.modular.entity.Master.ResidenceType;
import proj.modular.entity.Master.RetireInfo;
import proj.modular.entity.Master.Visit;
import proj.modular.entity.Master.work.WorkNews;
import proj.modular.vo.IndexVisitRecord;
import proj.modular.vo.index.*;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class IndexService {
  @Autowired
  private RetireInfoRepo retireInfoRepo;
  @Autowired
  private VisitRepo visitRepo;
  @Autowired
  private WorkNewsRepo workNewsRepo;

  public GenderDistVo genderDist() {
    return retireInfoRepo.getGenderDist();
  }

  private List<RetireInfo> getAgeCalcList() {
    return retireInfoRepo.getAgeCalcList(0).stream().map(DistCalcVo::castRetireInfo).collect(Collectors.toList());
  }

  public AgeDistVo ageDist() {
    AgeDistVo vo = new AgeDistVo();
    List<RetireInfo> retireList = getAgeCalcList();
    int[] ageLevel = new int[]{0, 0, 0, 0, 0, 0};
    retireList.stream().map(RetireInfo::getAge)
        .filter(Objects::nonNull)
        .forEach(age -> {
          int key = age / 10 > 9 ? 5 : age / 10 - 5;
          if (key >= 0) {
            ageLevel[key]++;
          }
        });
    vo.setA50to59(ageLevel[0]);
    vo.setA60to69(ageLevel[1]);
    vo.setA70to79(ageLevel[2]);
    vo.setA80to89(ageLevel[3]);
    vo.setA90to99(ageLevel[4]);
    vo.setOver100(ageLevel[5]);
    return vo;
  }

  public PartyCountVo partyCount() {
    return retireInfoRepo.getPartyCountVo();
  }

  public DeptDistVo deptDist() {
    DeptDistVo vo = new DeptDistVo();
    vo.setPartyBranch(retireInfoRepo.getPartyBranch());
    vo.setFunctionalBrigade(retireInfoRepo.getFunctionalBrigade().stream()
        .map(item -> new LabelValueVoImpl(
            item.getName().replaceAll("大队", ""), item.getValue())
        ).collect(Collectors.toList()));
    vo.setPoliceStation(retireInfoRepo.getPoliceStation().stream()
        .map(item -> new LabelValueVoImpl(
            item.getName().replaceAll("派出所", ""), item.getValue())
        ).collect(Collectors.toList()));

    List<String> lcArea = Arrays.asList("绣山","黎明","蒲鞋市","洪殿","南浦","江滨","南门","五马","莲池","水心","广化","黄龙","南郊","双屿","仰义","藤桥","临江","七都","中山","鞋都","上戍","世纪广场");
    vo.setResidentialPoliceStation(
        retireInfoRepo.getResidencePoliceDist().stream()
            .filter(label -> lcArea.contains(label.getName()))
            .collect(Collectors.toList())
    );
    return vo;
  }

  public SpecialCountVo specialCount() {
    return retireInfoRepo.getSpecialCountVo();
  }

  public List<IndexVisitRecord> getTop30VisitRecord() {
    List<IndexVisitRecord> res = new LinkedList<>();
    List<RetireInfo> allRetires = retireInfoRepo.findAllByIsDelIsFalseAndTypeEquals(0);
    List<String> allRetireIds = allRetires.stream().map(RetireInfo::getId).collect(Collectors.toList());
    List<Visit> visitList = visitRepo.getLatestRecords(allRetireIds, PageRequest.of(0, 30));

    if (visitList.size() > 0) {
      Set<String> retireIds = visitList.stream().map(Visit::getRetireId).collect(Collectors.toSet());
      Map<String, RetireInfo> retirePersons = retireInfoRepo.findAllById(retireIds).stream().collect(Collectors.toMap(RetireInfo::getId, r -> r));

//      Set<Integer> residenceRefIds = retirePersons.values().stream().filter(r -> r.getResidenceRefId() != null).map(RetireInfo::getResidenceRefId).collect(Collectors.toSet());
//      Map<Integer, ResidenceInfo> residenceMap = residenceInfoRepo.findAllByIdIn(residenceRefIds).stream().collect(Collectors.toMap(ResidenceInfo::getId, r -> r));

      for (Visit visit : visitList) {
        IndexVisitRecord vo = new IndexVisitRecord();
        vo.setVisitDate(visit.getVisitDate());
        RetireInfo visitPerson = retirePersons.get(visit.getRetireId());
        if (visitPerson != null) {
          vo.setVisit(visitPerson.getName());

//          if (visitPerson.getResidenceRefId() != null) {
//            ResidenceInfo residenceInfo = residenceMap.get(visitPerson.getResidenceRefId());
//
//            if (residenceInfo != null) {
//              vo.setVisitBy(residenceInfo.getPolice());
//              vo.setPoliceStation(residenceInfo.getPoliceStation());
//            } else {
//              vo.setVisitBy("不明");
//              vo.setPoliceStation("不明");
//            }
//          }
          vo.setVisitBy(visitPerson.getCommunityPolice());
          vo.setPoliceStation(visitPerson.getManageDept());
        }
        res.add(vo);
      }
    }
    return res;
  }

  public List<WorkNewsVo> getWorkNewsBrief(Integer size) {
    Page<WorkNews> news = workNewsRepo.findAll((e,cq,cb) -> cq.where(
        cb.isFalse(e.get("isDeleted"))
    ).orderBy(cb.desc(e.get("createAt")))
            .getRestriction(),
        PageRequest.of(0, size)
    );
    List<WorkNewsVo> res = new LinkedList<>();
    Optional.ofNullable(news.getContent()).ifPresent(list -> res.addAll(list.stream().map(item -> {
      WorkNewsVo brief = new WorkNewsVo();
      BeanUtils.copyProperties(item, brief);
      //截取image标签值
      String html = item.getContent();
      int tagStart = html.indexOf("<img");
      if (tagStart > -1) {
        tagStart += 5;
        int tagEnd = html.indexOf(">", tagStart);
        String tag = item.getContent().substring(tagStart, tagEnd);

        int srcStart = tag.indexOf("src=\"");
        if (srcStart > -1) {
          srcStart += 5;
          int srcEnd = tag.indexOf("\"", srcStart);
          brief.setImgUrl(tag.substring(srcStart, srcEnd));
        }
      }
      return brief;
    }).collect(Collectors.toList())));
    return res;
  }

  public List<LabelValueVo> policeAgeDist() {
    List<LabelValueVo> res = new ArrayList<>();
    Map<String, Integer> rangeCntMap = new HashMap<>();
    List<RetireInfo> retireList = getAgeCalcList();
    retireList.stream().map(RetireInfo::getPoliceAge)
        .filter(Objects::nonNull)
        .forEach(age -> {
          int range = age / 10;
          String mapName = 10 * range + "-" + 10 * (range + 1) + "年";
          if (range >= 4) {
            mapName = "40年及以上";
          }
          rangeCntMap.put(mapName, rangeCntMap.getOrDefault(mapName, 0) + 1);
        });
    rangeCntMap.putIfAbsent("0-10年", 0);
    rangeCntMap.putIfAbsent("10-20年", 0);
    rangeCntMap.putIfAbsent("20-30年", 0);
    rangeCntMap.putIfAbsent("30-40年", 0);
    rangeCntMap.putIfAbsent("40年及以上", 0);
    rangeCntMap.forEach((k, v) -> res.add(new LabelValueVoImpl(k, v)));
    res.sort(Comparator.comparing(LabelValueVo::getName));
    return res;
  }

  public List<LabelValueVo> partyAgeDist() {
    List<LabelValueVo> res = new ArrayList<>();
    Map<String, Integer> rangeCntMap = new HashMap<>();
    List<RetireInfo> retireList = getAgeCalcList();
    retireList.stream().map(RetireInfo::getPartyAge)
        .filter(Objects::nonNull)
        .forEach(age -> {
          int range = age / 10;
          String mapName = 10 * range + "-" + 10 * (range + 1) + "年";
          if (range >= 5) {
            mapName = "50年及以上";
          } else if (range < 2) {
            mapName = "0-20年";
          }
          rangeCntMap.put(mapName, rangeCntMap.getOrDefault(mapName, 0) + 1);
        });
    rangeCntMap.putIfAbsent("0-20年", 0);
    rangeCntMap.putIfAbsent("20-30年", 0);
    rangeCntMap.putIfAbsent("30-40年", 0);
    rangeCntMap.putIfAbsent("40-50年", 0);
    rangeCntMap.putIfAbsent("50年及以上", 0);
    rangeCntMap.forEach((k, v) -> res.add(new LabelValueVoImpl(k, v)));
    res.sort(Comparator.comparing(LabelValueVo::getName));

    //排序后将第一个替换key值
    LabelValueVo first = res.get(0);
    res.remove(0);
    res.add(0, new LabelValueVoImpl("20年及以下", first.getValue()));
    return res;
  }

  public List<LabelValueVo> residenceTypeDist() {
    List<RetireInfo> retireList = getAgeCalcList();
    List<LabelValueVo> res = retireList.stream().map(RetireInfo::getResidenceType)
        .filter(Objects::nonNull)
        .collect(Collectors.groupingBy(type -> type, Collectors.counting()))
        .entrySet().stream()
        .map(entry -> new LabelValueVoImpl(entry.getKey().getLabel(), entry.getValue()))
        .collect(Collectors.toList());

    //添加为0的额外数据
    List<ResidenceType> extra = Arrays.stream(ResidenceType.values()).filter(t ->
        res.stream().map(LabelValueVo::getName).noneMatch(label -> label.equals(t.getLabel()))
    ).collect(Collectors.toList());
    for (ResidenceType type : extra) {
      res.add(new LabelValueVoImpl(type.getLabel(), 0));
    }
    return res;
  }

  public List<LabelValueVo> activeDist() {
    return retireInfoRepo.activeRateDist();
  }
}
