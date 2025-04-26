package proj.util.Shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import proj.modular.dao.Master.auth.PermissionRepo;
import proj.modular.dao.Master.auth.RolePermissionRepo;
import proj.modular.dao.Master.auth.RoleRepo;
import proj.modular.dao.Master.auth.UserRoleRepo;
import proj.modular.entity.Master.User;
import proj.modular.entity.Master.auth.Permission;
import proj.modular.entity.Master.auth.Role;
import proj.modular.entity.Master.auth.RolePermissionRef;
import proj.modular.entity.Master.auth.UserRoleRef;
import proj.modular.service.UserService;
import proj.util.Redis.Redis;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户-角色-权限 关系 所处的"环境"
 */
public class CustomRealm extends AuthorizingRealm {
  @Autowired
  private Redis redis;
  @Autowired
  private UserService userService;
  @Autowired
  private RoleRepo roleRepo;
  @Autowired
  private UserRoleRepo userRoleRepo;
  @Autowired
  private PermissionRepo permissionRepo;
  @Autowired
  private RolePermissionRepo rolePermissionRepo;

  /**
   * 根据用户获取角色列表
   * @param user
   * @return
   */
  public List<Role> getRoles(User user) {
    List<UserRoleRef> refs = userRoleRepo.findAllByUserId(user.getId());
    List<Integer> roleIds = new ArrayList<>();
    for (UserRoleRef ref : refs) {
      roleIds.add(ref.getRoleId());
    }
    if (roleIds.size() > 0) {
      return roleRepo.findAllByIdIn(roleIds);
    }
    return new ArrayList<>(0);
  }

  /**
   * 根据角色获取权限列表
   * @param roles
   * @return
   */
  public List<Permission> getPermissions(Collection<Role> roles) {
    List<Integer> roleIds = roles.stream().map(Role::getId).collect(Collectors.toList());
    if (roleIds.size() > 0) {
      List<RolePermissionRef> refs = rolePermissionRepo.findAllByRoleIdIn(roleIds);
      if (refs.size() > 0) {
        List<Integer> permissionIds = refs.stream().map(RolePermissionRef::getPermissionId).collect(Collectors.toList());
        return permissionRepo.findAllByIdIn(permissionIds);
      }
    }
    return new ArrayList<>(0);
  }

  /**
   * 授权(每次检测是否具备接口权限时触发)
   *
   * @param principalCollection
   * @return
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    //获取登录用户名
    String username = (String) principalCollection.getPrimaryPrincipal();
    User user = userService.getOneByUsername(username);

    //获取角色
    Set<String> roleSet = new HashSet<>();
    String rolesCacheKey = "lbb/" + user.getId() + "/roles";
    List<Role> roles = redis.getList(rolesCacheKey, Role.class);
    if (roles == null) {
      roles = getRoles(user);
      redis.set(rolesCacheKey, roles);
      redis.expire(rolesCacheKey, 10L, TimeUnit.SECONDS);
    }
    for (Role role : roles) {
      roleSet.add(role.getName());
    }

    //获取权限
    Set<String> permissionSet = new HashSet<>();
    String permissionsCacheKey = "lbb/" + user.getId() + "/permissions";
    List<Permission> permissions = redis.getList(permissionsCacheKey, Permission.class);
    if (permissions == null) {
      permissions = getPermissions(roles);
      redis.set(permissionsCacheKey, permissions);
      redis.expire(permissionsCacheKey, 10L, TimeUnit.SECONDS);
    }
    for (Permission permission : permissions) {
      permissionSet.add(permission.getUri());
    }

    //添加角色权限到simpleAuthInfo
    SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
    simpleAuthorizationInfo.addRoles(roleSet);
    simpleAuthorizationInfo.addStringPermissions(permissionSet);
    return simpleAuthorizationInfo;
  }

  /**
   * 认证
   *
   * @param authenticationToken
   * @return
   * @throws AuthenticationException
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
    if (authenticationToken.getPrincipal() == null) {
      return null;
    }

    //获取用户信息
    String username = authenticationToken.getPrincipal().toString();
    User user = userService.getOneByUsername(username);
    return new SimpleAuthenticationInfo(username, user.getPwd(), getName());
  }

  @Bean
  public static DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
    return new DefaultAdvisorAutoProxyCreator();
  }
}
