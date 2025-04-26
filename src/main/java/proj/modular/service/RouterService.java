package proj.modular.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proj.modular.dao.Master.RoleRouterRepo;
import proj.modular.dao.Master.RouterRepo;
import proj.modular.dao.Master.auth.UserRoleRepo;
import proj.modular.dto.router.RouterDto;
import proj.modular.entity.Master.RoleRouterRef;
import proj.modular.entity.Master.Router;
import proj.modular.entity.Master.auth.UserRoleRef;
import proj.modular.vo.router.MetaVo;
import proj.modular.vo.router.RouterVo;
import proj.util.Log;
import proj.util.ShiroUtils;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RouterService {
  @Autowired
  private RouterRepo routerRepo;
  @Autowired
  private Log logger;
  @Autowired
  private ShiroUtils shiroUtils;
  @Autowired
  private UserRoleRepo userRoleRepo;
  @Autowired
  private RoleRouterRepo roleRouterRepo;

  // 嵌套dto转线性entity
  private List<Router> dtoList2EntityList(String parentId, List<RouterDto> dtos) {
    List<Router> list = new LinkedList<>();
    for (RouterDto dto : dtos) {
      Router r = new Router();
      r.setId(UUID.randomUUID().toString());
      BeanUtils.copyProperties(dto, r);
      r.setCreateAt(new Date());
      r.setCreateBy("系统");
      r.setParentId(parentId);
      if (dto.getMeta() != null) {
        r.setTitle(dto.getMeta().getTitle());
        r.setIcon(dto.getMeta().getIcon());
      }
      list.add(r);
      if (dto.getChildren() != null && dto.getChildren().size() > 0) {
        list.addAll(dtoList2EntityList(r.getId(), dto.getChildren()));
      }
    }
    return list;
  }

  // 线性entity转嵌套vo
  private List<RouterVo> entityList2VoList(String parentId, List<Router> routerList) {
    List<RouterVo> list = new LinkedList<>();
    routerList.stream()
      .filter(r -> Objects.equals(parentId, r.getParentId()))
      .forEach(r -> {
        RouterVo vo = new RouterVo();
        BeanUtils.copyProperties(r, vo);
        vo.setChildren(entityList2VoList(r.getId(), routerList));
        if (r.getTitle() != null || r.getIcon() != null) {
          MetaVo meta = new MetaVo();
          meta.setTitle(r.getTitle());
          meta.setIcon(r.getIcon());
          vo.setMeta(meta);
        }
        list.add(vo);
      });
    return list;
  }

  @Transactional
  public void initRouters(List<RouterDto> dtos) {
    routerRepo.deleteAll();
    if (dtos.size() > 0) {
      List<Router> list = dtoList2EntityList(null, dtos);
      int sort = 0;
      for (Router router : list) {
        router.setSort(++sort);
      }
      routerRepo.saveAll(list);
    }
    logger.info("路由初始化完毕");
  }

  public List<RouterVo> getRoleRouters(Integer roleId) {
    List<RoleRouterRef> refs = roleRouterRepo.findAllByRoleId(roleId);
    if (refs.size() > 0) {
      List<String> routerIds = refs.stream().map(RoleRouterRef::getRouterId).collect(Collectors.toList());
      List<Router> routers = routerRepo.findAllByIdIn(routerIds);
      routers.sort(Comparator.comparing(Router::getSort));
      return entityList2VoList(null, routers);
    }
    return new ArrayList<>(0);
  }

  public List<RouterVo> getUserRouters() {
    String userId = shiroUtils.getCurUserId();
    List<UserRoleRef> userRoleRefs = userRoleRepo.findAllByUserId(userId);
    if (userRoleRefs.size() > 0) {
      Set<Integer> roleIds = userRoleRefs.stream().map(UserRoleRef::getRoleId).collect(Collectors.toSet());
      List<RoleRouterRef> roleRouterRefs = roleRouterRepo.findAllByRoleIdIn(roleIds);
      if (roleRouterRefs.size() > 0) {
        Set<String> routerIds = roleRouterRefs.stream().map(RoleRouterRef::getRouterId).collect(Collectors.toSet());
        List<Router> routerList = routerRepo.findAllById(routerIds);
        routerList.sort(Comparator.comparing(Router::getSort));
        return entityList2VoList(null, routerList);
      }
    }
    return new ArrayList<>();
  }

  public List<RouterVo> getList() {
    return entityList2VoList(null, routerRepo.findAll());
  }

  @Transactional
  public void bind(Integer roleId, List<String> routerIds) {
    roleRouterRepo.deleteByRoleId(roleId);
    if (routerIds.size() > 0) {
      List<RoleRouterRef> refs = new LinkedList<>();
      for (String routerId : routerIds) {
        RoleRouterRef ref = new RoleRouterRef();
        ref.setRoleId(roleId);
        ref.setRouterId(routerId);
        refs.add(ref);
      }
      roleRouterRepo.saveAll(refs);
    }
  }
}
