package proj.modular.service;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import proj.modular.dao.Master.DeptRepo;
import proj.modular.dao.Master.PoliceStationRepo;
import proj.modular.dao.Master.UserRepo;
import proj.modular.dao.Master.auth.RoleRepo;
import proj.modular.dao.Master.auth.UserRoleRepo;
import proj.modular.dto.UserDto;
import proj.modular.dto.UserPageDto;
import proj.modular.entity.Master.Dept;
import proj.modular.entity.Master.PoliceStation;
import proj.modular.entity.Master.User;
import proj.modular.entity.Master.auth.Role;
import proj.modular.entity.Master.auth.UserRoleRef;
import proj.modular.vo.UserVo;
import proj.util.Configuration.Constant;
import proj.util.ShiroUtils;
import proj.util.pubMethods;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {
  private final UserRepo userRepo;
  private final UserRoleRepo refRepo;
  private final RoleRepo roleRepo;
  private final DeptRepo deptRepo;
  private final ShiroUtils shiroUtils;
  private final Constant constant;
  private final PoliceStationRepo policeStationRepo;

  public UserService(UserRepo userRepo, UserRoleRepo refRepo, RoleRepo roleRepo, DeptRepo deptRepo,
                     ShiroUtils shiroUtils, Constant constant, PoliceStationRepo policeStationRepo) {
    this.userRepo = userRepo;
    this.refRepo = refRepo;
    this.roleRepo = roleRepo;
    this.deptRepo = deptRepo;
    this.shiroUtils = shiroUtils;
    this.constant = constant;
    this.policeStationRepo = policeStationRepo;
  }

  public UserVo login(String username, String password) {
    try {
      password = pubMethods.pwdEncode(password, "MD5");
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    User user = getOneByUsername(username);
    if (validUser(user, password)) {
      shiroLogin(user);
      UserVo vo = new UserVo();
      BeanUtils.copyProperties(user, vo);
      vo.setRoles(getBoundRolesByUserId(vo.getId()));
      return vo;
    }
    return null;
  }

  private boolean validUser(User user, String password) {
    return user != null && !user.getIsDeleted() && user.getPwd().equals(password);
  }

  private void shiroLogin(User user) {
    UsernamePasswordToken token = new UsernamePasswordToken(user.getUsername(), user.getPwd());
    SecurityUtils.getSubject().login(token);
    SecurityUtils.getSubject().getSession().setTimeout(constant.getSessionExpired());

    // 带上可视部门列表
//    List<UserRoleRef> urRef = refRepo.findAllByUserId(user.getId());
//    List<Role> roles = roleRepo.findAllByIdIn(urRef.stream().map(UserRoleRef::getRoleId).collect(Collectors.toSet()));
//    Set<Dept> viewDepts = new HashSet<>(); //TODO 查询等
//    roles.forEach(role -> viewDepts.addAll(role.getViewDept()));
//    user.setViewDepts(viewDepts);

    shiroUtils.setSessionAttr("user", user);
  }

  public void logout() {
    SecurityUtils.getSubject().logout();
  }

  public User add(UserDto dto) {
    User user = new User();
    BeanUtils.copyProperties(dto, user);
    user.setId(UUID.randomUUID().toString());
    if (!StringUtils.isEmpty(user.getDeptId())) {
      Dept dept = deptRepo.findByDeptId(user.getDeptId());
      if (dept != null) {
        user.setDeptName(dept.getDeptName());
      }
    }
    try {
      user.setPwd(pubMethods.pwdEncode(dto.getPassword(), "MD5"));
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    user.setIsDeleted(false);
    user.setCreateBy(shiroUtils.getCurUserId());
    user.setCreateAt(new Date());
    return userRepo.save(user);
  }

  public User getOneByUsername(String username) {
    return userRepo.findByUsername(username);
  }

  public User getOneById(String id) {
    return userRepo.findById(id).orElse(null);
  }

  public User getOneByPoliceNum(String policeNum) {
    return userRepo.findByPoliceNum(policeNum);
  }

  public boolean isUserExisted(User user) {
    return user != null && !user.getIsDeleted();
  }

  public User update(User u, UserDto dto) {
    String deptId = dto.getDeptId();
    if (!StringUtils.isEmpty(deptId)) {
      Optional<Dept> deptOpt = deptRepo.findById(deptId);
      if (deptOpt.isPresent()) {
        u.setDeptId(deptId);
        u.setDeptName(deptOpt.get().getDeptName());
      }
    }
    if (!StringUtils.isEmpty(dto.getPassword())) {
      try {
        u.setPwd(pubMethods.pwdEncode(dto.getPassword(), "MD5"));
      } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
      }
    }
    u.setName(dto.getName());
    u.setUpdateAt(new Date());
    u.setUpdateBy(shiroUtils.getCurUserId());
    return userRepo.save(u);
  }

  public void deleteById(String id) {
    User u = userRepo.findById(id).orElse(null);
    if (u != null) {
      u.setIsDeleted(true);
      userRepo.save(u);
    }
  }

  @Transactional
  public void bindRole(String id, List<Integer> roleIds) {
    refRepo.deleteByUserId(id);
    if (roleIds.size() > 0) {
      List<UserRoleRef> refs = roleIds.stream().map(roleId -> {
        UserRoleRef ref = new UserRoleRef();
        ref.setUserId(id);
        ref.setRoleId(roleId);
        return ref;
      }).collect(Collectors.toList());
      refRepo.saveAll(refs);
    }
  }

  public Page<User> getList(UserPageDto dto) {
    return userRepo.findAll((e, cq, cb) -> cq.where(
      cb.and(
        cb.isFalse(e.get("isDeleted")),
        !StringUtils.isEmpty(dto.getName())? cb.like(e.get("name"), "%"+dto.getName()+"%"): cb.conjunction(),
        !StringUtils.isEmpty(dto.getUsername())? cb.like(e.get("username"), "%"+dto.getUsername()+"%"): cb.conjunction(),
        !StringUtils.isEmpty(dto.getDeptName())? cb.like(e.get("deptName"), "%"+dto.getDeptName()+"%"): cb.conjunction()
      )
    ).orderBy(cb.desc(e.get("createAt")))
      .getRestriction(), PageRequest.of(dto.getPage()-1, dto.getPageSize()));
  }

  public List<Dept> getDeptList() {
    return deptRepo.findAll();
  }

  public List<Role> getBoundRolesByUserId(String userId) {
    List<UserRoleRef> refs = refRepo.findAllByUserId(userId);
    if (refs.size() > 0) {
      List<Integer> roleIds = refs.stream().map(UserRoleRef::getRoleId).collect(Collectors.toList());
      return roleRepo.findAllByIdIn(roleIds);
    }
    return new ArrayList<>(0);
  }

  public List<PoliceStation> getPoliceStationList() {
    return policeStationRepo.findAll();
  }

  public void updateDeptView(User user, Set<String> deptIds) {
    List<Dept> depts = deptRepo.findAllById(deptIds);
    user.setViewDepts(new HashSet<>(depts));
    userRepo.save(user);
  }

  public Set<Dept> getDeptViewByUser(User user) {
    return user.getViewDepts();
  }

  public void updateManageDept(User user, Set<Integer> manageDeptIds) {
    List<PoliceStation> list = policeStationRepo.findAllById(manageDeptIds);
    user.setManageDepts(new HashSet<>(list));
    userRepo.save(user);
  }

  public Set<PoliceStation> getManageDeptByUser(User user) {
    return user.getManageDepts();
  }
}
