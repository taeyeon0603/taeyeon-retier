package proj.modular.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import proj.modular.dao.Master.DeptRepo;
import proj.modular.dao.Master.UserRepo;
import proj.modular.dao.Master.auth.PermissionRepo;
import proj.modular.dao.Master.auth.RolePermissionRepo;
import proj.modular.dao.Master.auth.RoleRepo;
import proj.modular.dto.RolePageDto;
import proj.modular.entity.Master.User;
import proj.modular.entity.Master.auth.Permission;
import proj.modular.entity.Master.auth.Role;
import proj.modular.entity.Master.auth.RolePermissionRef;
import proj.modular.vo.CascadeVo;
import proj.modular.vo.CheckablePermission;
import proj.modular.vo.RoleVo;
import proj.util.ShiroUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RoleService {
  @Autowired
  private UserRepo userRepo;
  @Autowired
  private DeptRepo deptRepo;
  @Autowired
  private RoleRepo roleRepo;
  @Autowired
  private PermissionRepo permissionRepo;
  @Autowired
  private RolePermissionRepo refRepo;
  @Autowired
  private ShiroUtils shiroUtils;

  public Role add(String roleName) {
    Role r = new Role();
    r.setName(roleName);
    r.setCreateAt(new Date());
    r.setCreateBy(shiroUtils.getCurUserId());
    return roleRepo.save(r);
  }

  public Role getOneByName(String roleName) {
    return roleRepo.findOneByName(roleName);
  }

  public Role getOneById(Integer roleId) {
    return roleRepo.findById(roleId).orElse(null);
  }

  public Role update(Role role, String roleName) {
    role.setName(roleName);
    role.setUpdateAt(new Date());
    role.setUpdateBy(shiroUtils.getCurUserId());
    return roleRepo.save(role);
  }

  @Transactional
  public void deleteById(Integer roleId) {
    roleRepo.deleteById(roleId);
  }

  public Page<RoleVo> getList(RolePageDto dto) {
    Pageable pageable = PageRequest.of(dto.getPage()-1, dto.getPageSize());
    Page<Role> roles = roleRepo.findAll((e, cq, cb) -> cq.where(
      !StringUtils.isEmpty(dto.getName())? cb.like(e.get("name"), "%"+dto.getName()+"%"): cb.conjunction()
    ).orderBy(cb.desc(e.get("id"))).getRestriction(), pageable);
    if (roles.getSize() > 0) {
      List<User> users = userRepo.findAll();
      Map<String, String> userNameMap = users.stream().collect(Collectors.toMap(User::getId, User::getName));
      Map<Integer, List<CascadeVo>> rolePermissions = findAllByRoleIds(roles.map(Role::getId).getContent());

      return new PageImpl<>(roles.stream().map(r -> {
        RoleVo vo = new RoleVo();
        BeanUtils.copyProperties(r, vo);
        if (!StringUtils.isEmpty(vo.getCreateBy())) {
          vo.setCreateBy(userNameMap.get(r.getCreateBy()));
        }
        if (!StringUtils.isEmpty(vo.getCreateBy())) {
          vo.setUpdateBy(userNameMap.get(r.getUpdateBy()));
        }
        vo.setPermissionGroup(rolePermissions.get(vo.getId()));
        return vo;
      }).collect(Collectors.toList()), pageable, roles.getTotalElements());
    }
    return new PageImpl<>(new ArrayList<>(0));
  }

  private Map<Integer, List<CascadeVo>> findAllByRoleIds(List<Integer> roleIds) {
    Map<Integer, List<CascadeVo>> res = new LinkedHashMap<>();
    List<RolePermissionRef> refs = refRepo.findAllByRoleIdIn(roleIds);
    List<Permission> permissions = permissionRepo.findAll();
    Map<Integer, List<Integer>> rolePermissionIds = refs.stream().collect(Collectors.groupingBy(RolePermissionRef::getRoleId, Collectors.mapping(RolePermissionRef::getPermissionId, Collectors.toList())));

    for (Integer roleId : roleIds) {
      List<Integer> permissionIds = rolePermissionIds.get(roleId);
      if (permissionIds == null) {
        permissionIds = new ArrayList<>(0);
      }
      List<CheckablePermission> checkList = new LinkedList<>();
      for (Permission permission : permissions) {
        CheckablePermission cp = new CheckablePermission();
        BeanUtils.copyProperties(permission, cp);
        cp.setChecked(permissionIds.contains(permission.getId()));
        checkList.add(cp);
      }
      List<CascadeVo> groupList = new LinkedList<>();
      checkList.stream().collect(Collectors.groupingBy(CheckablePermission::getGroupName))
        .forEach((groupName, children) -> {
          CascadeVo<CheckablePermission> group = new CascadeVo<>();
          group.setValue(children.get(0).getGroupKey());
          group.setLabel(groupName);
          group.setChildren(children);
          groupList.add(group);
        });
      res.put(roleId, groupList);
    }
    return res;
  }

  @Transactional
  public void updatePermissions(Integer roleId, List<Integer> permissionIds) {
    refRepo.deleteByRoleId(roleId);
    if (permissionIds.size() > 0) {
      List<RolePermissionRef> refs = permissionIds.stream().distinct().map(pId -> {
        RolePermissionRef ref = new RolePermissionRef();
        ref.setRoleId(roleId);
        ref.setPermissionId(pId);
        return ref;
      }).collect(Collectors.toList());
      refRepo.saveAll(refs);
    }
  }
}
