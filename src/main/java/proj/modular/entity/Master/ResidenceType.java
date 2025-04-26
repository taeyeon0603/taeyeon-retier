package proj.modular.entity.Master;

import lombok.Getter;

@Getter
public enum ResidenceType {
  InDistrict("区内"),
  InCity("区外市内"),
  InProvince("市外省内"),
  OutsideProvince("省外"),
  Foreign("国外");

  private String label;

  ResidenceType(String label) {
    this.label = label;
  }

  public static ResidenceType findName(String label) {
    for (ResidenceType type : ResidenceType.values()) {
      if (type.label.equals(label)) {
        return type;
      }
    }
    return null;
  }
}
