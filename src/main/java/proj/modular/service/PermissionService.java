package proj.modular.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proj.modular.dao.Master.auth.PermissionRepo;
import proj.modular.entity.Master.auth.Permission;
import proj.modular.vo.CascadeVo;
import proj.util.ShiroUtils;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PermissionService {
  @Autowired
  private PermissionRepo permissionRepo;
  @Autowired
  private ShiroUtils shiroUtils;

  public List<CascadeVo> getGroups() {
    List<CascadeVo> res = new LinkedList<>();
    List<Permission> permissions = permissionRepo.findAll();
    if (permissions.size() > 0) {
      permissions.stream().collect(Collectors.groupingBy(Permission::getGroupName))
        .forEach((groupName, children) -> {
          CascadeVo<Permission> c = new CascadeVo<>();
          c.setValue(children.get(0).getGroupKey());
          c.setLabel(groupName);
          c.setChildren(children);
          res.add(c);
        });
    }
    return res;
  }

  public Permission getOneByNameInGroup(String name, String groupName) {
    return permissionRepo.findByNameAndGroupName(name, groupName);
  }

  public Permission add(String name, String uri, String groupName) {
    Permission p = new Permission();
    p.setName(name);
    p.setUri(uri);
    p.setGroupName(groupName);
    p.setCreateAt(new Date());
    p.setCreateBy(shiroUtils.getCurUserId());
    return permissionRepo.save(p);
  }

  public Permission getOneById(Integer id) {
    return permissionRepo.findById(id).orElse(null);
  }

  public Permission update(Permission p, String name, String groupName) {
    p.setName(name);
    p.setGroupName(groupName);
    p.setUpdateAt(new Date());
    p.setUpdateBy(shiroUtils.getCurUserId());
    return permissionRepo.save(p);
  }

  public void deleteById(Integer id) {
    permissionRepo.deleteById(id);
  }
}
