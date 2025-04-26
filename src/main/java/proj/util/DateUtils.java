package proj.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class DateUtils {
  private static Map<String, DateTimeFormatter> formatterMap;
  private static List<String> patterns;

  static {
    formatterMap = new Hashtable<>();
    patterns = new CopyOnWriteArrayList<>();
  }

  private static DateTimeFormatter getFormatter(String pattern) {
    if (!patterns.contains(pattern)) {
      DateTimeFormatter formatter = new DateTimeFormatterBuilder()
        .appendPattern(pattern)
        .parseDefaulting(ChronoField.YEAR_OF_ERA, 1970)
        .parseDefaulting(ChronoField.MONTH_OF_YEAR, 2)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 2)
        .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .parseDefaulting(ChronoField.MILLI_OF_SECOND, 0)
        .toFormatter();
      patterns.add(pattern);
      formatterMap.put(pattern, formatter);
      return formatter;
    }
    return formatterMap.get(pattern);
  }

  /**
   * 字符串转日期
   *
   * @param str 字符串
   * @return
   */
  public static Date str2Date(String str, String pattern) {
    if (str == null || str.equals("")) {
      return null;
    }
    return localDateTime2Date(LocalDateTime.parse(str, getFormatter(pattern)));
  }

  /**
   * 日期转字符串
   *
   * @param date 日期
   * @param ptn  字符串格式
   * @return
   */
  public static String date2str(Date date, String ptn) {
    if (date == null) {
      return "无";
    }
    LocalDateTime ldt = date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
    return ldt.format(getFormatter(ptn));
  }

  /**
   * localDate转Date
   *
   * @param localDate
   * @return
   */
  public static Date localDate2Date(LocalDate localDate) {
    return Date.from(localDate.atStartOfDay(ZoneOffset.UTC).toInstant());
  }

  /**
   * LocalDateTime转Date
   *
   * @param localDateTime
   * @return
   */
  public static Date localDateTime2Date(LocalDateTime localDateTime) {
    return Date.from(localDateTime.toInstant(ZoneOffset.UTC));
  }

  /**
   * AbsTime转Date
   *
   * @param absTime 绝对时标
   */
  public static Date absTimeToDate(Integer absTime) {
    LocalDateTime time = LocalDateTime.of(
      (absTime >> 26) + 2000
      , (absTime >> 22) & 15
      , (absTime >> 17) & 31
      , (absTime >> 12) & 31
      , (absTime >> 6) & 63
      , absTime & 63);
    return localDateTime2Date(time);
  }

  /**
   * localDateTime转yyyy-MM-dd HH24:mi:ss
   *
   * @param localDateTime 时间
   * @return
   */
  public static String localDateTime2Str(LocalDateTime localDateTime, String ptn) {
    if (ptn == null || "".equals(ptn)) {
      ptn = "yyyy-MM-dd hh:mm:ss";
    }
    DateTimeFormatter format = DateTimeFormatter.ofPattern(ptn);
    return localDateTime.format(format);
  }

  /**
   * localDateTime转yyyy-MM-dd
   *
   * @param localDateTime 时间
   * @return
   */
  public static String localDateTime2DateStr(LocalDateTime localDateTime) {
    int year = localDateTime.getYear();
    int month = localDateTime.getMonthValue();
    int day = localDateTime.getDayOfMonth();

    return year + "-" + (month < 10 ? "0" : "") + month + "-" + (day < 10 ? "0" : "") + day;
  }

  /**
   * 时间戳转时间字符串
   */
  public static String long2DateStr(Long l, String ptn) {
    if (l == null)
      return null;
    return localDateTime2Str(Instant.ofEpochMilli(l).atZone(ZoneOffset.UTC).toLocalDateTime(), ptn);
  }

  /**
   * 根据出生日期计算年龄
   *
   * @param birth 出生日期
   * @return
   */
  public static Integer calcAge(Date birth) {
    if (birth == null) {
      return 0;
    }
    LocalDate lBirth = birth.toInstant().atZone(ZoneOffset.UTC).toLocalDate();
    int age = Period.between(lBirth, LocalDate.now()).getYears();
    return age < 0? 0: age;
  }

  public static Date calcMinBirth(Integer age) {
    if (age == null) {
      return null;
    }
    return localDate2Date(LocalDate.now().minusYears(age));
  }
}
