package proj.modular.controller;

import com.alibaba.druid.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import proj.modular.entity.Master.auth.Permission;
import proj.modular.service.PermissionService;
import proj.util.enums.errors;
import proj.util.Tip;

@Api(tags = "权限")
@RestController
@RequestMapping("permission")
public class PermissionController {
  @Autowired
  private PermissionService service;

  /**
   * 根据权限开头来分类
   * @return
   */
  @ApiOperation("获取权限类别(级联信息)")
  @RequiresPermissions("permission:groups")
  @GetMapping("groups")
  public Tip getGroups() {
    return Tip.ok(service.getGroups());
  }

  @ApiOperation("添加权限[不建议使用, 分类BUG]")
  @RequiresPermissions("permission:add")
  @PostMapping("add")
  public Tip addPermission(String name, String uri, String groupName) {
    if (StringUtils.isEmpty(name)) {
      return Tip.fail(400, "权限名称不能为空");
    }
    if (StringUtils.isEmpty(groupName)) {
      return Tip.fail(400, "权限类别不能为空");
    }
    if (service.getOneByNameInGroup(name, groupName) != null) {
      return Tip.fail(500, "权限名称已存在");
    }
    return Tip.ok(service.add(name, uri, groupName));
  }

  @ApiOperation("查看权限")
  @RequiresPermissions("permission:getOne")
  @GetMapping("{id}")
  public Tip getPermissionById(@PathVariable Integer id) {
    if (id == null) {
      return Tip.fail(errors.IDEMPTY);
    }
    Permission p = service.getOneById(id);
    if (p == null) {
      return Tip.fail(500, "权限ID不存在");
    }
    return Tip.ok(p);
  }

  @ApiOperation("更新权限内容")
  @RequiresPermissions("permission:update")
  @PutMapping("{id}/update")
  public Tip updatePermission(@PathVariable Integer id, @RequestParam String name, @RequestParam String groupName) {
    if (id == null) {
      return Tip.fail(errors.IDEMPTY);
    }
    Permission p = service.getOneById(id);
    if (p == null) {
      return Tip.fail(500, "角色ID不存在");
    }
    if (!StringUtils.isEmpty(name) && !name.equals(p.getName()) &&
      service.getOneByNameInGroup(name, groupName) != null) {
      return Tip.fail(500, "权限名称已存在");
    }
    return Tip.ok(service.update(p, name, groupName));
  }

  @ApiOperation("删除权限")
  @RequiresPermissions("permission:delete")
  @DeleteMapping("{id}")
  public Tip deletePermissionById(@PathVariable Integer id) {
    if (id == null) {
      return Tip.fail(errors.IDEMPTY);
    }
    service.deleteById(id);
    return Tip.ok();
  }
}
