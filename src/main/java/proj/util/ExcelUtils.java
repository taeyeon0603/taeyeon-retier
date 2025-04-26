package proj.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.math.BigInteger;
import java.util.regex.Pattern;

public class ExcelUtils {

  /**
   * 根据不同情况获取Java类型值
   * @param cell XSSFCell类型单元格
   * @return 返回Object类型值
   * @since 2017-03-26 00:05:36
   */
  public static Object getJavaValue(Cell cell) {
    Object o = null;
    CellType cellType = cell.getCellType();
    switch (cellType) {
      case BLANK:
        o = "";
        break;
      case BOOLEAN:
        o = cell.getBooleanCellValue();
        break;
      case ERROR:
        o = "Bad value!";
        break;
      case NUMERIC:
        o = getValueOfNumericCell(cell);
        break;
      case FORMULA:
        try {
          o = getValueOfNumericCell(cell);
        } catch (IllegalStateException e) {
          try {
            o = cell.getRichStringCellValue().toString();
          } catch (IllegalStateException e2) {
            o = cell.getErrorCellValue();
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
        break;
      default:
        o = cell.getRichStringCellValue().getString();
    }
    return o;
  }


  // 获取数字类型的cell值
  private static Object getValueOfNumericCell(Cell cell) {
    boolean isDate = DateUtil.isCellDateFormatted(cell);
    Double d = cell.getNumericCellValue();
    if (isDate) {
      return cell.getDateCellValue();
    }
    return getRealStringValueOfDouble(d);
  }

  // 处理科学计数法与普通计数法的字符串显示，尽最大努力保持精度
  private static String getRealStringValueOfDouble(Double d) {
    String doubleStr = d.toString();
    boolean b = doubleStr.contains("E");
    int indexOfPoint = doubleStr.indexOf('.');
    if (b) {
      int indexOfE = doubleStr.indexOf('E');
      // 小数部分
      BigInteger xs = new BigInteger(doubleStr.substring(indexOfPoint + BigInteger.ONE.intValue(), indexOfE));
      // 指数
      int pow = Integer.valueOf(doubleStr.substring(indexOfE + BigInteger.ONE.intValue()));
      int xsLen = xs.toByteArray().length;
      int scale = xsLen - pow > 0 ? xsLen - pow : 0;
      doubleStr = String.format("%." + scale + "f", d);
    } else {
      java.util.regex.Pattern p = Pattern.compile(".0$");
      java.util.regex.Matcher m = p.matcher(doubleStr);
      if (m.find()) {
        doubleStr = doubleStr.replace(".0", "");
      }
    }
    return doubleStr;
  }
}
