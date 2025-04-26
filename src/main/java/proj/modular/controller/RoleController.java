package proj.modular.controller;

import com.alibaba.druid.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import proj.modular.dto.RolePageDto;
import proj.modular.dto.RolePermissionsDto;
import proj.modular.entity.Master.auth.Role;
import proj.modular.service.RoleService;
import proj.util.Tip;
import proj.util.enums.errors;

@Api(tags = "角色")
@RestController
@RequestMapping("role")
public class RoleController {
  @Autowired
  private RoleService service;

  @ApiOperation("角色列表")
  @RequiresPermissions("role:list")
  @PostMapping("list")
  public Tip getList(@RequestBody RolePageDto dto) {
    dto.setPage(dto.getPage() <= 0? 1: dto.getPage());
    dto.setPageSize(dto.getPageSize() <= 0? 10: dto.getPageSize());
    return Tip.ok(service.getList(dto));
  }

  @ApiOperation("添加角色")
  @RequiresPermissions("role:add")
  @PostMapping("add")
  public Tip addRole(String name) {
    if (StringUtils.isEmpty(name)) {
      return Tip.fail(400, "角色名称不能为空");
    }
    if (service.getOneByName(name) != null) {
      return Tip.fail(500, "角色名称已存在");
    }
    return Tip.ok(service.add(name));
  }

  @ApiOperation("查看角色")
  @RequiresPermissions("role:getOne")
  @GetMapping("{id}")
  public Tip getRoleById(@PathVariable Integer id) {
    if (id == null) {
      return Tip.fail(errors.IDEMPTY);
    }
    Role r = service.getOneById(id);
    if (r == null) {
      return Tip.fail(500, "角色ID不存在");
    }
    return Tip.ok(r);
  }

  @ApiOperation("修改角色")
  @RequiresPermissions("role:update")
  @PutMapping("{id}/update")
  public Tip updateRole(@PathVariable Integer id, @RequestParam String name) {
    if (id == null) {
      return Tip.fail(errors.IDEMPTY);
    }
    Role r = service.getOneById(id);
    if (r == null) {
      return Tip.fail(500, "角色ID不存在");
    }
    if (!r.getName().equals(name) && service.getOneByName(name) != null) {
      return Tip.fail(500, "角色名称已存在");
    }
    return Tip.ok(service.update(r, name));
  }

  @ApiOperation("删除角色")
  @RequiresPermissions("role:delete")
  @DeleteMapping("{id}")
  public Tip deleteRoleById(@PathVariable Integer id) {
    if (id == null) {
      return Tip.fail(errors.IDEMPTY);
    }
    service.deleteById(id);
    return Tip.ok();
  }

  @ApiOperation("修改角色权限")
  @RequiresPermissions("role:updatePermissions")
  @PostMapping("updatePermissions")
  public Tip updatePermissions(@RequestBody @Validated RolePermissionsDto dto) {
    service.updatePermissions(dto.getRoleId(), dto.getPermissions());
    return Tip.ok();
  }
}
