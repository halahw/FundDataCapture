package util;

import org.apache.commons.lang.time.DateFormatUtils;

public class DateUtil {

  public static String getDateyyyyMMdd() {
    return DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMdd");
  }

  public static String getDateyyyyMMddHHmmss() {
    return DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmmss");
  }
}
