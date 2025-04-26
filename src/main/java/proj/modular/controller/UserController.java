package proj.modular.controller;

import com.alibaba.druid.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import proj.modular.dto.*;
import proj.modular.entity.Master.Dept;
import proj.modular.entity.Master.PoliceStation;
import proj.modular.entity.Master.User;
import proj.modular.service.UserService;
import proj.util.Tip;
import proj.util.enums.errors;

import java.util.List;
import java.util.Set;

@Api(tags = "用户")
@RestController
@RequestMapping("user")
public class UserController {
  @Autowired
  private UserService service;

  @ApiOperation("获取所有部门")
  @GetMapping("deptList")
  public Tip getDeptList() {
    return Tip.ok(service.getDeptList());
  }

  @ApiOperation("获取所有辖区派出所")
  @GetMapping("policeStationList")
  public Tip<List<PoliceStation>> getPoliceStationList() {
    return Tip.ok(service.getPoliceStationList());
  }

  @ApiOperation("用户列表")
  @RequiresPermissions("user:list")
  @PostMapping("list")
  public Tip getUserList(@Validated @RequestBody UserPageDto dto) {
    dto.setPage(dto.getPage() <= 0? 1: dto.getPage());
    dto.setPageSize(dto.getPageSize() <= 0? 10: dto.getPageSize());
    return Tip.ok(service.getList(dto));
  }

  @ApiOperation("添加用户")
  @RequiresPermissions("user:add")
  @PostMapping("add")
  public Tip addUser(@Validated UserDto dto) {
    if (service.isUserExisted(service.getOneByUsername(dto.getUsername()))) {
      return Tip.fail(500, "账号已存在");
    }
    if (service.isUserExisted(service.getOneByPoliceNum(dto.getPoliceNum()))) {
      return Tip.fail(500, "警号已存在");
    }
    return Tip.ok(service.add(dto));
  }

  @ApiOperation("获取单个用户信息")
  @RequiresPermissions("user:getOne")
  @GetMapping("{id}")
  public Tip getUserById(@PathVariable String id) {
    if (StringUtils.isEmpty(id)) {
      return Tip.fail(errors.IDEMPTY);
    }
    User u = service.getOneById(id);
    if (!service.isUserExisted(u)) {
      return Tip.fail(500, "用户ID不存在");
    }
    return Tip.ok(u);
  }

  @ApiOperation("修改用户信息")
  @RequiresPermissions("user:update")
  @PutMapping("{id}/update")
  public Tip updateUser(@PathVariable String id, @RequestBody UserDto dto) {
    if (StringUtils.isEmpty(id)) {
      return Tip.fail(errors.IDEMPTY);
    }
    User u = service.getOneById(id);
    if (!service.isUserExisted(u)) {
      return Tip.fail(500, "用户ID不存在");
    }
    return Tip.ok(service.update(u, dto));
  }

  @ApiOperation("删除用户")
  @RequiresPermissions("user:delete")
  @DeleteMapping("{id}")
  public Tip deleteUserById(@PathVariable String id) {
    if (StringUtils.isEmpty(id)) {
      return Tip.fail(errors.IDEMPTY);
    }
    service.deleteById(id);
    return Tip.ok();
  }

  @ApiOperation("绑定角色")
  @RequiresPermissions("user:bindRole")
  @PostMapping("bindRole")
  public Tip bindRole(@RequestBody UserRolesDto dto) {
    if (StringUtils.isEmpty(dto.getUserId())) {
      return Tip.fail(errors.IDEMPTY);
    }
    service.bindRole(dto.getUserId(), dto.getRoles());
    return Tip.ok();
  }

  @ApiOperation("已綁定角色列表")
  @RequiresPermissions("user:roles")
  @GetMapping("{id}/roles")
  public Tip getRoleRefsByUserId(@PathVariable String id) {
    if (StringUtils.isEmpty(id)) {
      return Tip.fail(errors.IDEMPTY);
    }
    return Tip.ok(service.getBoundRolesByUserId(id));
  }

  @ApiOperation("修改可视部门")
  @RequiresPermissions("user:updateDeptView")
  @PostMapping("updateDeptView")
  public Tip updateDeptView(@RequestBody @Validated UserDeptDto dto) {
    User user = service.getOneById(dto.getUserId());
    if (user == null) {
      return Tip.fail(500, "未找到用户");
    }
    service.updateDeptView(user, dto.getDeptIds());
    return Tip.ok();
  }

  @ApiOperation("获取可视部门")
  @RequiresPermissions("user:getDeptView")
  @PostMapping("getDeptView")
  public Tip<Set<Dept>> getDeptView(@RequestParam String userId) {
    if (userId == null) {
      return Tip.fail(500, "用户ID不能为空");
    }
    User user = service.getOneById(userId);
    if (user == null) {
      return Tip.fail(500, "未找到用户");
    }
    return Tip.ok(service.getDeptViewByUser(user));
  }

  @ApiOperation("修改管辖派出所")
  @RequiresPermissions("user:updateManageDept")
  @PostMapping("updateManageDept")
  public Tip updateManageDept(@RequestBody @Validated UserManageDeptDto dto) {
    User user = service.getOneById(dto.getUserId());
    if (user == null) {
      return Tip.fail(500, "未找到用户");
    }
    service.updateManageDept(user, dto.getManageDeptIds());
    return Tip.ok();
  }

  @ApiOperation("获取管辖派出所")
  @RequiresPermissions("user:getManageDept")
  @PostMapping("getManageDept")
  public Tip<Set<PoliceStation>> getManageDept(@RequestParam String userId) {
    if (userId == null) {
      return Tip.fail(500, "用户ID不能为空");
    }
    User user = service.getOneById(userId);
    if (user == null) {
      return Tip.fail(500, "未找到用户");
    }
    return Tip.ok(service.getManageDeptByUser(user));
  }
}
