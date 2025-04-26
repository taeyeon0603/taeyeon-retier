package proj.modular.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import proj.modular.dto.work.WorkInfoDto;
import proj.modular.dto.work.WorkInfoSearchDto;
import proj.modular.dto.work.WorkNewsDto;
import proj.modular.dto.work.WorkNewsSearchDto;
import proj.modular.service.WorkService;
import proj.modular.vo.FileVo;
import proj.util.Annotation.Processor.OptType;
import proj.util.Annotation.UploadLog;
import proj.util.Tip;

import java.util.List;

@Api(tags = "工作")
@RequestMapping("work")
@RestController
public class WorkController {
  @Autowired
  private WorkService service;

  @ApiOperation("信息栏目")
  @PostMapping("infoList")
  @RequiresPermissions("work:info/list")
  @UploadLog(optType = OptType.query, module = "工作信息")
  public Tip getInfoList(@RequestBody WorkInfoSearchDto searchDto) {
    return Tip.ok(service.getInfoList(searchDto));
  }

  @ApiOperation("上传附件")
  @PostMapping("info/upload")
  @RequiresPermissions("work:info/upload")
  @UploadLog(optType = OptType.insert, module = "工作信息附件")
  public Tip upload(@RequestParam("file") MultipartFile[] files) {
    List<FileVo> uploadFiles = service.upload(files);
    if (uploadFiles.size() == 0) {
      return Tip.fail(500, "上传失败");
    }
    return Tip.ok(uploadFiles);
  }

  @ApiOperation("新增信息")
  @PostMapping("info/add")
  @RequiresPermissions("work:info/add")
  @UploadLog(optType = OptType.insert, module = "工作信息")
  public Tip addWorkInfo(@RequestBody WorkInfoDto dto) {
    service.saveWorkInfo(dto);
    return Tip.ok();
  }

  @ApiOperation("删除信息")
  @PostMapping("info/del/{id}")
  @RequiresPermissions("work:info/del")
  @UploadLog(optType = OptType.delete, module = "工作信息")
  public Tip delWorkInfo(@PathVariable String id) {
    service.delWorkInfo(id);
    return Tip.ok();
  }

  @ApiOperation("动态栏目")
  @PostMapping("newsList")
  @RequiresPermissions("work:news/list")
  @UploadLog(optType = OptType.query, module = "工作动态")
  public Tip getNewsList(@RequestBody WorkNewsSearchDto searchDto) {
    return Tip.ok(service.getNewsList(searchDto));
  }

  @ApiOperation("新增动态")
  @PostMapping("news/add")
  @RequiresPermissions("work:news/add")
  @UploadLog(optType = OptType.insert, module = "工作动态")
  public Tip addWorkNews(@RequestBody WorkNewsDto dto) {
    service.saveWorkNews(dto);
    return Tip.ok();
  }

  @ApiOperation("删除动态")
  @PostMapping("news/del/{id}")
  @RequiresPermissions("work:news/del")
  @UploadLog(optType = OptType.delete, module = "工作动态")
  public Tip delWorkNews(@PathVariable String id) {
    service.delWorkNews(id);
    return Tip.ok();
  }
}
