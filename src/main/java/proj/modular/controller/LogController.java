package proj.modular.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import proj.modular.dto.LogSearchDto;
import proj.modular.service.LogService;
import proj.modular.vo.SimpleLogVo;
import proj.util.Annotation.Processor.OptType;
import proj.util.Annotation.UploadLog;
import proj.util.Tip;

@Api(tags = "日志")
@RestController
@RequestMapping("log")
public class LogController {
  private final LogService service;

  public LogController(LogService service) {
    this.service = service;
  }

  @PostMapping("list")
  @ApiOperation("获取日志列表")
  @UploadLog(optType = OptType.query, module = "日志列表")
  public Tip<Page<SimpleLogVo>> getList(@RequestBody LogSearchDto dto) {
    return Tip.ok(service.getList(dto));
  }
}
