package proj.util;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import proj.util.Configuration.Constant;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一些该项目的公共方法类
 */
@Component
public class pubMethods {
  @Autowired
  private Constant constant;
  @Autowired
  private Log logger;

  private static pubMethods pubMethods;

  public pubMethods() {
    pubMethods = this;
    pubMethods.constant = constant;
  }

  /**
   * 字符串转日期
   *
   * @param str 字符串内容
   * @param fmt 日期格式
   * @return
   */
  public static Date str2Date(String str, String fmt) {
    if (str == null || str.equals("")) {
      return null;
    }
    DateTimeFormatter df = DateTimeFormatter.ofPattern(fmt);
    LocalDate localDate = LocalDate.parse(str, df);
    ZoneId zoneId = ZoneId.systemDefault();
    ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
    return Date.from(zdt.toInstant());
  }

  /**
   * 使用LinkedHashMap对map的value进行排序后重新封装
   *
   * @param oriMap 原Map
   * @return 排序后的map
   */
  public static Map<String, Long> sortByValue(Map<String, Long> oriMap, String filterStr) {
    Map<String, Long> sortedData = new LinkedHashMap<>();

    oriMap.entrySet().stream().filter(entry -> !entry.getKey().equals(filterStr))
      .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
      .forEachOrdered(map -> sortedData.put(map.getKey(), map.getValue()));

    if (oriMap.get(filterStr) != null && oriMap.get(filterStr) > 0) {
      sortedData.put(filterStr, oriMap.get(filterStr));
    }

    return sortedData;
  }

  /**
   * 是否为鹿城区的派出所
   *
   * @param unitCode 单位编号
   * @return true or false
   */
  public static Boolean isWenzhouPolice(String unitCode) {
    //过滤非pcs部分的单位
    Pattern pattern = Pattern.compile("[0-9]*");
    Matcher isNum = pattern.matcher(unitCode);

    if (!isNum.matches()) {
      return false;
    }

    Long code = Long.parseLong(unitCode);

    return code >= 330302510000L && code <= 330302900000L;
  }

  /**
   * 将 yyyyMMddHHmmss 转换为 yyyy-MM-dd HH:mm:ss
   *
   * @param date 日期的字符串
   * @return 转换后的字符串
   */
  public static String DateStrFormat(String date) {
    return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8) +
      " " + date.substring(8, 10) + ":" + date.substring(10, 12) + ":" + date.substring(12, 14);
  }

  /**
   * 简化IP 去除多余的0
   *
   * @param IP 原IP
   * @return
   */
  public static String simplyIP(String IP) {
    StringBuilder sb = new StringBuilder();
    String arrIP[] = IP.split("\\.");

    if (arrIP.length > 0) {
      for (int pos = 0; pos < 3 && pos < arrIP.length - 1; pos++) {
        sb.append(Integer.parseInt(arrIP[pos])).append(".");
      }

      if (IP.indexOf('-') != -1) {
        String interval[] = IP.split("-");
        String part1[] = interval[0].split("\\.");
        sb.append(part1[part1.length - 1]).append("-").append(simplyIP(interval[1]));
      } else {
        sb.append(Integer.parseInt(arrIP[arrIP.length - 1]));
      }
    }

    return sb.toString();
  }

  /**
   * 过滤手机号码 替换为*
   *
   * @param content
   * @return
   */
  public static String filterPhoneNumber(String content) {
    if (content == null || content.equals("")) {
      return content;
    }

    String regexp = "";
    String mobilePhoneRegexp = "(?:(\\(\\+?86\\))((1[0-9]{1}[0-9]{1})|(15[0-9]{1})|(18[0,5-9]{1}))+\\d{8})|" +
      "(?:86-?((1[0-9]{1}[0-9]{1})|(15[0-9]{1})|(18[0,5-9]{1}))+\\d{8})|" +
      "(?:((1[0-9]{1}[0-9]{1})|(15[0-9]{1})|(18[0,5-9]{1}))+\\d{8})";
    String landlinePhoneRegexp = "(?:(\\(\\+?86\\))(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)|" +
      "(?:(86-?)?(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?)";
    regexp += "(?:" + mobilePhoneRegexp + "|" + landlinePhoneRegexp + ")";

    return Pattern.compile(regexp).matcher(content).replaceAll("*");
  }

  /**
   * 获取请求方IP
   * 每一层都获取
   *
   * @return
   */
  public static String getRealIPAddr() {
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

    if (attributes != null) {
      HttpServletRequest request = attributes.getRequest();
      String[] headers = pubMethods.constant.getHeadersToTry().split(",");

      JSONObject ipJson = new JSONObject();
      for (String header : headers) {
        ipJson.put(header, request.getHeader(header));
      }

      if (!StringUtils.isEmpty(request.getRemoteAddr())) {
        return request.getRemoteAddr();
      }
      return ipJson.values().stream().filter(Objects::nonNull).findFirst().map(Object::toString).orElse(request.getRemoteAddr());
    }

    return null;
  }

  /**
   * 获取相差n天的日期
   *
   * @param date 起始日期
   * @param day  相差天数 往前数的话传负数
   * @return Date
   */
  public static Date dateDiff(Date date, Integer day) {
    return Date.from(date.toInstant().plus(day, ChronoUnit.DAYS));
  }

  /**
   * 将bytes字符串转为byte[]
   *
   * @param bytes 如 [-1,2]
   * @return
   */
  public static byte[] bytesStr2bytes(String bytes) {
    String[] array = bytes.substring(1, bytes.length() - 1).split(",");
    byte[] res = new byte[array.length];

    if (array.length > 0) {
      if (array[0].contains("[")) {
        array[0] = array[0].replaceAll("\\[", "");
      }

      if (array[array.length - 1].contains("]")) {
        array[array.length - 1] = array[array.length - 1].replaceAll("\\]", "");
      }
    }

    for (int i = 0; i < array.length; i++) {
      res[i] = new Byte(array[i].trim());
    }

    return res;
  }

  /**
   * 将LocalTime转Date
   *
   * @param localDateTime
   * @return
   */
  public static Date localDateTime2Date(LocalDateTime localDateTime) {
    ZoneId zoneId = ZoneId.systemDefault();
    ZonedDateTime zdt = localDateTime.atZone(zoneId);
    return Date.from(zdt.toInstant());
  }

  /**
   * 将Date转LocalDateTime
   *
   * @param date 时间
   * @return
   */
  public static LocalDateTime date2LocalDateTime(Date date) {
    Instant instant = date.toInstant();
    ZoneId zoneId = ZoneId.systemDefault();
    return LocalDateTime.ofInstant(instant, zoneId);
  }

  /**
   * 封装简单PageInfo
   *
   * @param res      封装对象
   * @param page     页码
   * @param pageSize 页大小
   * @param total    记录总数
   * @param content  内容
   */
  public static void packageSimplePageInfoJson(JSONObject res, Integer page, Integer pageSize, Integer total,
                                               List content) {
    Integer totalPage = (total - 1) / pageSize + 1;

    res.put("listContent", content);
    res.put("total", total);
    res.put("totalPage", totalPage);
    res.put("current", page > totalPage ? totalPage : page);
    res.put("pageSize", pageSize);
  }

  /**
   * 密码加密
   *
   * @param pwd       密码
   * @param algorithm 加密算法
   * @return
   */
  public static String pwdEncode(String pwd, String algorithm)
    throws NoSuchAlgorithmException {
    MessageDigest md = MessageDigest.getInstance(algorithm);
    md.update(pwd.getBytes());

    //将加密后的数据转换为16进制数字
    StringBuilder md5code = new StringBuilder(new BigInteger(1, md.digest()).toString(16));// 16进制数字

    // 如果生成数字未满32位，需要前面补0
    for (int i = 0; i < 32 - md5code.length(); i++) {
      md5code.insert(0, "0");
    }
    return md5code.toString();
  }

  /**
   * 列表转字符串格式( ',' 分隔)
   * 用于sql in查询
   *
   * @param list
   * @return
   */
  public static String list2Str(List<String> list) {
    String res = list.toString();
    res = "'" + res.substring(1, res.length() - 1) + "'";
    return res.replace(" ", "").replace(",", "','");
  }

  public static String Arrays2String(String[] array) {
    StringBuilder sb = new StringBuilder(Arrays.toString(array));
    if (sb.indexOf("[") == 0 && sb.length() > 1) {
      sb.deleteCharAt(0);
    }
    if (sb.indexOf("]") == sb.length() - 1) {
      sb.deleteCharAt(sb.length() - 1);
    }
    String res = sb.toString();
    res = res.replaceAll(", ", ",");
    return res;
  }

  public static String fuzzy(String value) {
    return "%" + value + "%";
  }
}
