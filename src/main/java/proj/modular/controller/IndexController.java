package proj.modular.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proj.modular.service.IndexService;
import proj.modular.vo.IndexVisitRecord;
import proj.modular.vo.index.*;
import proj.util.Tip;

import java.util.List;

@Api(tags = "大屏")
@RequestMapping("index")
@RestController
public class IndexController {
  @Autowired
  private IndexService indexService;

  @ApiOperation("性别分布")
  @GetMapping("genderDist")
  public Tip<GenderDistVo> getGenderDistribution() {
    return Tip.ok(indexService.genderDist());
  }

  @ApiOperation("年龄分布")
  @GetMapping("ageDist")
  public Tip<AgeDistVo> getAgeDistribution() {
    return Tip.ok(indexService.ageDist());
  }

  @ApiOperation("党员数量")
  @GetMapping("partyCount")
  public Tip<PartyCountVo> getPartyCount() {
    return Tip.ok(indexService.partyCount());
  }

  @ApiOperation("单位分布")
  @GetMapping("deptDist")
  public Tip<DeptDistVo> getDeptDist() {
    return Tip.ok(indexService.deptDist());
  }

  @ApiOperation("特殊人群")
  @GetMapping("specialCount")
  public Tip getSpecialCount() {
    return Tip.ok(indexService.specialCount());
  }

  @ApiOperation("走访记录")
  @PostMapping("visitRecord")
  public Tip<List<IndexVisitRecord>> getVisitRecord() {
    return Tip.ok(indexService.getTop30VisitRecord());
  }

  @ApiOperation("滚播")
  @GetMapping("workNews")
  public Tip<List<WorkNewsVo>> getWorkNewsBrief() {
    return Tip.ok(indexService.getWorkNewsBrief(20));
  }

  @ApiOperation("居住地分布统计")
  @GetMapping("residenceTypeDist")
  public Tip<List<LabelValueVo>> getResidenceTypeDist() {
    return Tip.ok(indexService.residenceTypeDist());
  }

  @ApiOperation("警龄统计")
  @GetMapping("policeAgeDist")
  public Tip<List<LabelValueVo>> getPoliceAgeDist() {
    return Tip.ok(indexService.policeAgeDist());
  }

  @ApiOperation("党龄统计")
  @GetMapping("partyAgeDist")
  public Tip<List<LabelValueVo>> getPartyAgeDist() {
    return Tip.ok(indexService.partyAgeDist());
  }

  @ApiOperation("活跃指数统计")
  @GetMapping("activeDist")
  public Tip<List<LabelValueVo>> getActiveDist() {
    return Tip.ok(indexService.activeDist());
  }
}
