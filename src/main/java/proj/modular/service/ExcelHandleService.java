package proj.modular.service;

import com.alibaba.druid.util.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import proj.modular.dao.Master.DeptRepo;
import proj.modular.dto.retire.ResumeImportDto;
import proj.modular.dto.retire.RetireDto;
import proj.modular.dto.retire.RewardImportDto;
import proj.modular.entity.ExcelHandleRes;
import proj.modular.entity.Master.Dept;
import proj.util.ExcelUtils;
import proj.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ExcelHandleService {
  private final Log logger;
  private final DeptRepo deptRepo;

  public ExcelHandleService(Log logger, DeptRepo deptRepo) {
    this.logger = logger;
    this.deptRepo = deptRepo;
  }

  public ExcelHandleRes convertHandleRes(MultipartFile file) {
    ExcelHandleRes res = new ExcelHandleRes();

    try {
      String fileName = file.getOriginalFilename();
      String fileType = fileName != null ? fileName.substring(fileName.lastIndexOf('.') + 1) : "xls";
      Workbook workbook = getWorkbook(file.getInputStream(), fileType);

      if (workbook == null) {
        res.setIsSuccess(false);
        res.getMsg().add("Excel 转换失败");
        return res;
      }
      Sheet retireSheet = workbook.getSheet("退休人员");
      Sheet resumeSheet = workbook.getSheet("履历");
      Sheet rewardSheet = workbook.getSheet("获奖");

      parseRetireDto(retireSheet, res);
      parseResumeDto(resumeSheet, res);
      parseRewardDto(rewardSheet, res);
    } catch (IOException e) {
      logger.error(e);
    }
    return res;
  }

  private void parseRetireDto(Sheet sheet, ExcelHandleRes res) {
    if (sheet == null) {
      res.setIsSuccess(false);
      res.getMsg().add("Sheet\"退休人员\"获取失败");
      return;
    }
    Map<String, Dept> deptMap = deptRepo.findAll().stream().collect(Collectors.toMap(Dept::getDeptName, d -> d));

    Row firstRow = sheet.getRow(sheet.getTopRow());
    sheet.rowIterator().forEachRemaining(row -> {
      RetireDto dto = new RetireDto();
      int rowInd = row.getRowNum();
      if (rowInd == 0) {
        return;
      }
      AtomicInteger fillSize = new AtomicInteger(0);
      row.cellIterator().forEachRemaining(cell -> {
        int colInd = cell.getColumnIndex();
        try {
          Object value = ExcelUtils.getJavaValue(cell);
          fillSize.incrementAndGet();
          switch (firstRow.getCell(colInd).getStringCellValue()) {
            case "姓名": dto.setName(value.toString()); break;
            case "性别": dto.setGender(value.toString()); break;
            case "身份证号": dto.setIdCard(value.toString()); break;
            case "警号": dto.setPoliceCode(value.toString()); break;
            case "手机号码": dto.setTel(value.toString()); break;
            case "政治面貌": dto.setPoliticalIdentity(value.toString()); break;
            case "短号": dto.setShortTel(value.toString()); break;
            case "实际居住地": dto.setResidenceAddr(value.toString()); break;
            case "居住类型": dto.setResidenceType(value.toString()); break;
            case "所属党支部": dto.setParty(value.toString()); break;
            case "原单位":
              if (!StringUtils.isEmpty(value.toString())) {
                String deptName = value.toString().replaceAll(" ", "");
                Dept dept = deptMap.get(deptName);
                if (dept != null) {
                  dto.setDeptId(dept.getDeptId());
                  dto.setDeptName(dept.getDeptName());
                } else {
                  res.getMsg().add("第" + rowInd + "行未匹配到原单位名称");
                }
              }
              break;
            case "原职级": dto.setLevel(value.toString()); break;
            case "原职务": dto.setPost(value.toString()); break;
            case "疾病": dto.setDisease(value.toString()); break;
            case "银行卡号": dto.setPensionBankCard(value.toString()); break;
            case "家属联系方式": dto.setFixedTel(value.toString()); break;
            case "健康状况": dto.setHealthStatus(value.toString()); break;
            case "家庭情况": dto.setFamilyCondition(value.toString()); break;
            case "生活困难": dto.setIsLifeHard("是".equals(value.toString())); break;
            case "孤寡老人": dto.setIsLonelyOldMan("是".equals(value.toString())); break;
            case "文体爱好": dto.setActivities(value.toString()); break;
            case "辖区派出所": dto.setPoliceStation(value.toString()); break;
            case "所属社区": dto.setCommunity(value.toString()); break;
            case "社区民警": dto.setPolice(value.toString()); break;
            case "社区民警短号": dto.setPoliceTel(value.toString()); break;
            case "备注": dto.setRemark(value.toString()); break;
            case "入党时间":
              if (!ObjectUtils.isEmpty(value)) {
                dto.setJoinPartyDate((Date) value);
              }
              break;
            case "退休时间":
              if (!ObjectUtils.isEmpty(value)) {
                dto.setRetireDate((Date) value);
              }
              break;
            default: fillSize.decrementAndGet();
          }
        } catch (Exception e) {
          res.getMsg().add("人员信息的第" + rowInd + "行第" + colInd + "列表数据获取异常");
        }
      });
      if (fillSize.get() == 0) {
        res.setIsSuccess(false);
        res.getMsg().add("人员信息的第" + rowInd + "行数据为空");
      } else {
        res.getRetireList().add(dto);
      }
    });
    if (res.getRetireList().size() == 0) {
      res.getMsg().add("退休人员列表为空");
    }
  }

  private void parseResumeDto(Sheet sheet, ExcelHandleRes res) {
    if (sheet == null) {
      res.setIsSuccess(false);
      res.getMsg().add("Sheet\"履历\"获取失败");
      return;
    }

    Row firstRow = sheet.getRow(sheet.getTopRow());
    sheet.rowIterator().forEachRemaining(row -> {
      ResumeImportDto dto = new ResumeImportDto();
      int rowInd = row.getRowNum();
      if (rowInd == 0) {
        return;
      }
      AtomicInteger fillSize = new AtomicInteger(0);
      row.cellIterator().forEachRemaining(cell -> {
        int colInd = cell.getColumnIndex();
        fillSize.incrementAndGet();
        try {
          switch (firstRow.getCell(colInd).getStringCellValue()) {
            case "身份证号": dto.setIdCard(cell.getStringCellValue()); break;
            case "所在单位": dto.setDeptName(cell.getStringCellValue()); break;
            case "开始时间": Optional.ofNullable(cell.getDateCellValue()).ifPresent(dto::setStartTime);break;
            case "结束时间": Optional.ofNullable(cell.getDateCellValue()).ifPresent(dto::setEndTime); break;
            default: fillSize.decrementAndGet();
          }
        } catch (Exception e) {
          res.getMsg().add("履历的第" + rowInd + "行第" + colInd + "列表数据获取异常");
        }
      });
      if (fillSize.get() == 0) {
        res.setIsSuccess(false);
        res.getMsg().add("履历的第" + rowInd + "行数据为空, 请检查文件");
      } else {
        res.getResumes().add(dto);
      }
    });
    if (res.getResumes().size() == 0) {
      res.getMsg().add("履历列表为空");
    }
  }

  private void parseRewardDto(Sheet sheet, ExcelHandleRes res) {
    if (sheet == null) {
      res.setIsSuccess(false);
      res.getMsg().add("Sheet\"获奖\"获取失败");
      return;
    }
    Row firstRow = sheet.getRow(sheet.getTopRow());
    sheet.rowIterator().forEachRemaining(row -> {
      RewardImportDto dto = new RewardImportDto();
      int rowInd = row.getRowNum();
      if (rowInd == 0) {
        return;
      }
      AtomicInteger fillSize = new AtomicInteger(0);
      row.cellIterator().forEachRemaining(cell -> {
        int colInd = cell.getColumnIndex();
        fillSize.incrementAndGet();
        try {
          switch (firstRow.getCell(colInd).getStringCellValue()) {
            case "身份证号": dto.setIdCard(cell.getStringCellValue()); break;
            case "所在单位": dto.setDept(cell.getStringCellValue()); break;
            case "批准机关": dto.setApproveAddress(cell.getStringCellValue()); break;
            case "批准机关级别": dto.setApproveLevel(cell.getStringCellValue()); break;
            case "获奖发文号": dto.setCodeNumber(cell.getStringCellValue()); break;
            case "获奖文件名": dto.setLinkAddress(cell.getStringCellValue()); break;
            case "集体|个人": dto.setTypes("集体".equals(cell.getStringCellValue())?"jt":"gr"); break;
            case "奖励类型": dto.setRewardType(cell.getStringCellValue()); break;
            case "获奖日期": Optional.ofNullable(cell.getDateCellValue()).ifPresent(dto::setRewardDate);break;
            default: fillSize.decrementAndGet();
          }
        } catch (Exception e) {
          res.getMsg().add("获奖信息的第" + rowInd + "行第" + colInd + "列表数据获取异常");
        }
      });
      if (fillSize.get() == 0) {
        res.setIsSuccess(false);
        res.getMsg().add("获奖信息的第" + rowInd + "行数据为空, 请检查文件");
      } else {
        res.getRewards().add(dto);
      }
    });
    if (res.getRewards().size() == 0) {
      res.getMsg().add("获奖列表为空");
    }
  }

  private Workbook getWorkbook(InputStream in, String fileType) {
    Workbook workbook = null;
    try {
      if (fileType.equalsIgnoreCase("xls")) {
        workbook = new HSSFWorkbook(in);
      } else if (fileType.equalsIgnoreCase("xlsx")) {
        workbook = new XSSFWorkbook(in);
      }
    } catch (IOException e) {
      logger.error(e);
    }
    return workbook;
  }
}
