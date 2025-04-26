package proj.modular.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import proj.modular.dto.router.RouterDto;
import proj.modular.service.RouterService;
import proj.util.Tip;

import java.util.List;

@Api(tags = "路由")
@RestController
@RequestMapping("router")
public class RouterController {
  @Autowired
  private RouterService service;

  @ApiOperation("路由表初始化(慎用，会先清空路由表)")
  @PostMapping("init")
  public Tip initRouters(@RequestBody List<RouterDto> dtos) {
    service.initRouters(dtos);
    return Tip.ok();
  }

  @ApiOperation("获取全部路由")
  @GetMapping("list")
  public Tip getList() {
    return Tip.ok(service.getList());
  }

  @ApiOperation("角色路由表")
  @GetMapping("{roleId}/list")
  public Tip getListByRoleId(@PathVariable Integer roleId) {
    return Tip.ok(service.getRoleRouters(roleId));
  }

  @ApiOperation("当前用户的路由表")
  @GetMapping("boundList")
  public Tip getRouterByUserId() {
    return Tip.ok(service.getUserRouters());
  }

  @ApiOperation("角色绑定路由")
  @PostMapping("{roleId}/bind")
  public Tip bindRouter(@PathVariable Integer roleId, @RequestParam List<String> routerIds) {
    service.bind(roleId, routerIds);
    return Tip.ok();
  }
}
