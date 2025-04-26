package proj.modular.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import proj.modular.entity.Master.ResidenceType;
import proj.modular.service.DictService;
import proj.util.Tip;

@Api(tags = "字典")
@RequestMapping("dict")
@RestController
public class DictController {
  @Autowired
  private DictService service;

  @ApiOperation("获取党支部列表")
  @PostMapping("party/deptList")
  public Tip getListByType() {
    return Tip.ok(service.getListByType("党支部"));
  }

  @ApiOperation("获取居住地类型")
  @GetMapping("residence/type")
  public Tip getResidenceType() {
    return Tip.ok(ResidenceType.values());
  }
}
