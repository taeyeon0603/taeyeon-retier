package proj.modular.vo.index;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter @Getter
public class DeptDistVo {
  private List<LabelValueVo> partyBranch;
  private List<LabelValueVoImpl> functionalBrigade;
  private List<LabelValueVoImpl> policeStation;
  private List<LabelValueVo> residentialPoliceStation;
}
