package proj.modular.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proj.modular.dao.Master.DictRepo;
import proj.modular.vo.DictVo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DictService {
  @Autowired
  private DictRepo dictRepo;

  public List<DictVo> getListByType(String type) {
    return dictRepo.findAllByType(type).stream().map(dict -> {
      DictVo vo = new DictVo();
      BeanUtils.copyProperties(dict, vo);
      return vo;
    }).collect(Collectors.toList());
  }
}
