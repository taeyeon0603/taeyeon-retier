package proj.modular.entity;

import lombok.Getter;
import lombok.Setter;
import proj.modular.dto.retire.ResumeImportDto;
import proj.modular.dto.retire.RetireDto;
import proj.modular.dto.retire.RewardImportDto;

import java.util.LinkedList;
import java.util.List;

@Setter @Getter
public class ExcelHandleRes {
  private Boolean isSuccess;
  private List<String> msg;
  private List<RetireDto> retireList;
  private List<ResumeImportDto> resumes;
  private List<RewardImportDto> rewards;

  public ExcelHandleRes() {
    isSuccess = true;
    msg = new LinkedList<>();
    retireList = new LinkedList<>();
    resumes = new LinkedList<>();
    rewards = new LinkedList<>();
  }
}
