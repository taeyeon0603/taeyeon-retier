package proj.modular.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import proj.modular.dto.retire.*;
import proj.modular.entity.ExcelHandleRes;
import proj.modular.entity.Master.RetireInfo;
import proj.modular.service.ExcelHandleService;
import proj.modular.service.PoliceInfoService;
import proj.util.Annotation.Processor.OptType;
import proj.util.Annotation.UploadLog;
import proj.util.Tip;
import proj.util.enums.RetirePoliceType;

import java.util.List;

@Api(tags = "人员信息")
@RequestMapping("policeInfo")
@RestController
public class PoliceInfoController {
  private final PoliceInfoService service;
  private final ExcelHandleService excelHandleService;

  public PoliceInfoController(PoliceInfoService service, ExcelHandleService excelHandleService) {
    this.service = service;
    this.excelHandleService = excelHandleService;
  }

  @ApiOperation("退休人员照片上传")
  @PostMapping("retire/uploadPic")
  @UploadLog(optType = OptType.insert, module = "退休人员照片")
  public Tip uploadPic(@RequestParam("file") MultipartFile file) {
    String filepath = service.uploadPic(file);
    if (filepath == null) {
      return Tip.fail(500, "图标上传失败");
    }
    return Tip.ok(filepath);
  }

  @ApiOperation("退休人员列表")
  @PostMapping("retireList")
  @UploadLog(optType = OptType.query, module = "退休人员")
  public Tip getPage(@RequestBody RetirePageDto dto) {
    return Tip.ok(service.getPage(dto, dto.getIsHistory() ? RetirePoliceType.History : RetirePoliceType.Normal));
  }

  @ApiOperation("退休新人列表")
  @PostMapping("retireList/new")
  @UploadLog(optType = OptType.query, module = "退休新人")
  public Tip getNewPage(@RequestBody RetirePageDto dto) {
    return Tip.ok(service.getPage(dto, RetirePoliceType.New));
  }

  @ApiOperation("编外退休人员列表")
  @PostMapping("retireList/extra")
  @UploadLog(optType = OptType.query, module = "编外退休人员")
  public Tip getExtraPage(@RequestBody RetirePageDto dto) {
    return Tip.ok(service.getPage(dto, RetirePoliceType.Extra));
  }

  @ApiOperation("退休录入")
  @PostMapping("retire")
  @UploadLog(optType = OptType.insert, module = "退休人员")
  public Tip addRetire(@RequestBody @Validated RetireDto dto) {
    RetireInfo retire = service.save(dto, dto.getIsHistory() ? RetirePoliceType.History : RetirePoliceType.Normal);
    return retire != null ? Tip.ok(retire.getId()) : Tip.fail(500, "录入失败");
  }

  @ApiOperation("新人退休录入")
  @PostMapping("retire/new")
  @UploadLog(optType = OptType.insert, module = "退休新人")
  public Tip addRetireNew(@RequestBody @Validated RetireDto dto) {
    RetireInfo retire = service.save(dto, RetirePoliceType.New);
    return retire.getId() != null ? Tip.ok(retire.getId()) : Tip.fail(500, "录入失败");
  }

  @ApiOperation("编外人员退休录入")
  @PostMapping("retire/extra")
  @UploadLog(optType = OptType.insert, module = "编外退休人员")
  public Tip addRetireExtra(@RequestBody @Validated RetireDto dto) {
    RetireInfo retire = service.save(dto, RetirePoliceType.Extra);
    return retire != null ? Tip.ok(retire.getId()) : Tip.fail(500, "录入失败");
  }

  @ApiOperation("退休删除")
  @DeleteMapping("retire/{retireId}/delete")
  @UploadLog(optType = OptType.delete, module = "退休人员")
  public Tip deleteRetire(@PathVariable String retireId) {
    RetireInfo retireInfo = service.findById(retireId);
    if (retireInfo == null) {
      return Tip.fail(500, "未找到当前操作的退休对象");
    }
    return service.deleteRetireInfo(retireInfo) ? Tip.ok() : Tip.fail(500, "删除失败");
  }

  @ApiOperation("奖励情况录入")
  @PostMapping("{retireId}/updateReward")
  @UploadLog(optType = OptType.update, module = "退休人员获奖情况")
  public Tip updateReward(@PathVariable String retireId, @RequestBody @Validated List<RewardDto> dtoList) {
    RetireInfo retireInfo = service.findById(retireId);
    if (retireInfo == null) {
      return Tip.fail(500, "未找到当前操作的退休对象");
    }
    return service.updateReward(retireInfo, dtoList) ? Tip.ok() : Tip.fail(500, "录入失败");
  }

  @ApiOperation("履历录入")
  @PostMapping("{retireId}/updateResume")
  @UploadLog(optType = OptType.update, module = "退休人员履历")
  public Tip updateResume(@PathVariable String retireId, @RequestBody @Validated List<ResumeDto> dtoList) {
    RetireInfo retireInfo = service.findById(retireId);
    if (retireInfo == null) {
      return Tip.fail(500, "未找到当前操作的退休对象");
    }
    return service.updateResume(retireInfo, dtoList) ? Tip.ok() : Tip.fail(500, "录入失败");
  }

  @ApiOperation("退休人员全部数据")
  @PostMapping("retireList/all")
  @UploadLog(optType = OptType.query, module = "批量退休人员")
  public Tip getAll(@RequestBody RetireSearchDto dto) {
    return Tip.ok(service.getAll(dto, dto.getIsHistory() ? RetirePoliceType.History : RetirePoliceType.Normal));
  }

  @ApiOperation("退休新人全部数据")
  @PostMapping("retireList/all/new")
  @UploadLog(optType = OptType.query, module = "批量退休新人")
  public Tip getNewAll(@RequestBody RetireSearchDto dto) {
    return Tip.ok(service.getAll(dto, RetirePoliceType.New));
  }

  @ApiOperation("编外退休人员全部数据")
  @PostMapping("retireList/all/extra")
  @UploadLog(optType = OptType.query, module = "批量编外退休人员")
  public Tip getExtraAll(@RequestBody RetireSearchDto dto) {
    return Tip.ok(service.getAll(dto, RetirePoliceType.Extra));
  }

  @ApiOperation("退休人员信息导入")
  @PostMapping("import")
  @UploadLog(optType = OptType.insert, module = "批量退休人员")
  public Tip<ExcelHandleRes> importExcel(MultipartFile file) {
    if (file == null) {
      return Tip.fail(500, "文件不能为空");
    }
    ExcelHandleRes handleRes = excelHandleService.convertHandleRes(file);
    service.importExcelHandleData(handleRes);
    return Tip.ok(handleRes);
  }
}
