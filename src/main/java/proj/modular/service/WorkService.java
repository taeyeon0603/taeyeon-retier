package proj.modular.service;

import com.alibaba.druid.util.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import proj.modular.dao.Master.UserRepo;
import proj.modular.dao.Master.work.WorkFileRepo;
import proj.modular.dao.Master.work.WorkInfoRepo;
import proj.modular.dao.Master.work.WorkNewsRepo;
import proj.modular.dto.FileDto;
import proj.modular.dto.work.WorkInfoDto;
import proj.modular.dto.work.WorkInfoSearchDto;
import proj.modular.dto.work.WorkNewsDto;
import proj.modular.dto.work.WorkNewsSearchDto;
import proj.modular.entity.Master.User;
import proj.modular.entity.Master.work.WorkFile;
import proj.modular.entity.Master.work.WorkInfo;
import proj.modular.entity.Master.work.WorkNews;
import proj.modular.vo.FileVo;
import proj.modular.vo.retire.work.WorkInfoVo;
import proj.modular.vo.retire.work.WorkNewsVo;
import proj.util.Configuration.Constant;
import proj.util.Log;
import proj.util.ShiroUtils;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkService {
  @Autowired
  private WorkInfoRepo workInfoRepo;
  @Autowired
  private WorkFileRepo workFileRepo;
  @Autowired
  private WorkNewsRepo workNewsRepo;
  @Autowired
  private UserRepo userRepo;
  @Autowired
  private ShiroUtils shiroUtils;
  @Autowired
  private Constant constant;
  @Autowired
  private Log logger;

  private Map<String, String> getUserMap() {
    List<User> users = userRepo.findAll();
    return users.stream().collect(Collectors.toMap(User::getId, User::getName));
  }

  public Page<WorkInfoVo> getInfoList(WorkInfoSearchDto searchDto) {
    Pageable pageable = PageRequest.of(searchDto.getPage()-1, searchDto.getPageSize());
    Page<WorkInfo> list = workInfoRepo.findAll((e,cq,cb) -> cq.where(
      cb.and(
        !StringUtils.isEmpty(searchDto.getTitle())? cb.like(e.get("title"), "%"+searchDto.getTitle()+"%"): cb.conjunction(),
        cb.isFalse(e.get("isDeleted"))
      )
    ).orderBy(cb.desc(e.get("id")).reverse()).getRestriction(), pageable);

    Map<String, String> userMap = getUserMap();
    List<WorkFile> wfList = workFileRepo.findAllByRefIdIn(list.stream().map(WorkInfo::getId).collect(Collectors.toList()));
    Map<String, List<FileVo>> infoFileVoMap = wfList.stream().collect(Collectors.groupingBy(
        WorkFile::getRefId,
        Collectors.mapping(wf -> {
          FileVo v = new FileVo();
          v.setName(wf.getName());
          v.setPath(wf.getPath());
          return v;
        }, Collectors.toList())
    ));

    return new PageImpl<>(list.stream().map(w -> {
      WorkInfoVo vo = new WorkInfoVo();
      BeanUtils.copyProperties(w, vo);
      vo.setCreateBy(userMap.get(vo.getCreateBy()));
      vo.setFiles(infoFileVoMap.get(w.getId()));
      return vo;
    }).collect(Collectors.toList()), pageable, list.getTotalElements());
  }

  public Page<WorkNewsVo> getNewsList(WorkNewsSearchDto searchDto) {
    Pageable pageable = PageRequest.of(searchDto.getPage()-1, searchDto.getPageSize());
    Page<WorkNews> list = workNewsRepo.findAll((e, cq, cb) -> cq.where(
      cb.and(
        !StringUtils.isEmpty(searchDto.getTitle())? cb.like(e.get("title"), "%"+searchDto.getTitle()+"%"): cb.conjunction(),
        !StringUtils.isEmpty(searchDto.getContent())? cb.like(e.get("content"), "%"+searchDto.getContent()+"%"): cb.conjunction(),
        cb.isFalse(e.get("isDeleted"))
      )
    ).orderBy(cb.desc(e.get("id")).reverse()).getRestriction(), pageable);

    Map<String, String> userMap = getUserMap();
    List<WorkFile> wfList = workFileRepo.findAllByRefIdIn(list.stream().map(WorkNews::getId).collect(Collectors.toList()));
    Map<String, List<FileVo>> newsFileVoMap = wfList.stream().collect(Collectors.groupingBy(
      WorkFile::getRefId,
      Collectors.mapping(wf -> {
        FileVo v = new FileVo();
        v.setName(wf.getName());
        v.setPath(wf.getPath());
        return v;
      }, Collectors.toList())
    ));

    return new PageImpl<>(list.stream().map(w -> {
      WorkNewsVo vo = new WorkNewsVo();
      BeanUtils.copyProperties(w, vo);
      vo.setCreateBy(userMap.get(vo.getCreateBy()));
      vo.setFiles(newsFileVoMap.get(w.getId()));
      return vo;
    }).collect(Collectors.toList()), pageable, list.getTotalElements());
  }

  public List<FileVo> upload(MultipartFile[] files) {
    //图片文件夹路径确认
    File path = new File(constant.getWorkFilePath());
    if (!path.isDirectory()) {
      path.mkdirs();
    }
    List<FileVo> res = new LinkedList<>();

    try {
      List<WorkFile> wfList = new LinkedList<>();
      for (MultipartFile file : files) {
        String oriFilename = file.getOriginalFilename();
        if (oriFilename != null) {
          String filename = UUID.randomUUID().toString() + "." + oriFilename.substring(oriFilename.lastIndexOf(".")+1);
          File dst = new File(path.getAbsolutePath() + "/" + filename);
          file.transferTo(dst);
          String filePath = constant.getWorkFileUrl() + filename;

          FileVo vo = new FileVo();
          vo.setName(oriFilename);
          vo.setPath(filePath);
          res.add(vo);

          WorkFile wf = new WorkFile();
          wf.setId(UUID.randomUUID().toString());
          wf.setName(oriFilename);
          wf.setPath(filePath);
          wfList.add(wf);

          // todo 文件删除工作 定时对近1个月 不存在关联RefId的记录中的文件进行删除
        }
      }
      if (wfList.size() > 0) {
        workFileRepo.saveAll(wfList);
      }
    } catch (IOException e) {
      logger.error(e);
    }

    return res;
  }

  @Transactional
  public void saveWorkInfo(WorkInfoDto dto) {
    WorkInfo info = new WorkInfo();
    BeanUtils.copyProperties(dto, info);
    if (StringUtils.isEmpty(info.getId())) {
      info.setId(UUID.randomUUID().toString());
    }
    info.setIsDeleted(false);
    info.setCreateAt(new Date());
    info.setCreateBy(shiroUtils.getCurUserId());
    workInfoRepo.save(info);

    rebindWorkFile(dto.getFiles(), info.getId());
  }

  //工作文件关联重新绑定
  private void rebindWorkFile(List<FileDto> files, String refId) {
    List<WorkFile> handleList = workFileRepo.findAllByRefId(refId);
    for (WorkFile workFile : handleList) {
      workFile.setRefId(null);
    }
    if (handleList.size() > 0) {
      workFileRepo.saveAll(handleList);
    }
    handleList.clear();
    List<WorkFile> wfListWithNoRefId = workFileRepo.findAllByRefIdIsNull();
    for (WorkFile wf : wfListWithNoRefId) {
      if (files.stream().anyMatch(f -> f.getName().equals(wf.getName()) && f.getPath().equals(wf.getPath()))) {
        wf.setRefId(refId);
        handleList.add(wf);
      }
    }
    if (handleList.size() > 0) {
      workFileRepo.saveAll(handleList);
    }

  }

  public void delWorkInfo(String id) {
    WorkInfo info = workInfoRepo.findById(id).orElse(null);
    if (info != null) {
      info.setIsDeleted(true);
      workInfoRepo.save(info);
    }
  }

  @Transactional
  public void saveWorkNews(WorkNewsDto dto) {
    WorkNews news = new WorkNews();
    BeanUtils.copyProperties(dto, news);
    if (StringUtils.isEmpty(news.getId())) {
      news.setId(UUID.randomUUID().toString());
    }
    news.setIsDeleted(false);
    news.setCreateAt(new Date());
    news.setCreateBy(shiroUtils.getCurUserId());
    workNewsRepo.save(news);

    rebindWorkFile(dto.getFiles(), news.getId());
  }

  public void delWorkNews(String id) {
    WorkNews news = workNewsRepo.findById(id).orElse(null);
    if (news != null) {
      news.setIsDeleted(true);
      workNewsRepo.save(news);
    }
  }
}
