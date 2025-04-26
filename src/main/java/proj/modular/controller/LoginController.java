package proj.modular.controller;

import com.alibaba.druid.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import proj.modular.service.UserService;
import proj.modular.vo.UserVo;
import proj.util.Annotation.Processor.OptType;
import proj.util.Annotation.UploadLog;
import proj.util.Tip;

@Api(tags = "登录")
@RestController
public class LoginController {
  @Autowired
  private UserService service;

  @ApiOperation("登录")
  @PostMapping("login")
  @UploadLog(optType = OptType.login, module = "用户")
  public Tip login(@RequestParam String username, @RequestParam String password) {
    if (StringUtils.isEmpty(username)) {
      return Tip.fail(400, "用户名不能为空");
    }
    if (StringUtils.isEmpty(password)) {
      return Tip.fail(400, "密码不能为空");
    }
    UserVo user = service.login(username, password);
    if (user == null) {
      return Tip.fail(500, "用户名或密码错误");
    }
    return Tip.ok(user);
  }

  @ApiOperation("退出")
  @PostMapping("logout")
  public Tip logout() {
    service.logout();
    return Tip.ok();
  }
}
