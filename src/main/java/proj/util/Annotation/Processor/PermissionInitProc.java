package proj.util.Annotation.Processor;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import proj.modular.dao.Master.auth.PermissionRepo;
import proj.modular.entity.Master.auth.Permission;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j(topic = "app-log")
@Configuration
public class PermissionInitProc implements BeanPostProcessor {
  @Autowired
  private PermissionRepo permissionRepo;
  private List<Permission> permissionList;
  private Map<String, String> groupMap;

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    if (permissionList == null) {
      permissionList = permissionRepo.findAll();
      groupMap = new HashMap<>();
      for (Permission p : permissionList) {
        groupMap.putIfAbsent(p.getGroupKey(), p.getGroupName());
      }
    }
    Class clz = bean.getClass();
    if (clz.isAnnotationPresent(RestController.class) ||
      clz.isAnnotationPresent(Controller.class)) {
      Method[] methods = clz.getDeclaredMethods();
      for (Method method : methods) {
        RequiresPermissions permission = method.getAnnotation(RequiresPermissions.class);
        if (permission != null) {
          for (String v : permission.value()) {
            if (permissionList.stream().noneMatch(p -> p.getUri().equals(v))) {
              Permission newPermission = new Permission();
              newPermission.setUri(v);
              String groupName = groupMap.get(newPermission.getGroupKey());
              newPermission.setGroupName(groupName == null? "未分组": groupName);
              newPermission.setCreateAt(new Date());
              permissionRepo.save(newPermission);
            }
          }
        }
      }
    }
    return bean;
  }
}
