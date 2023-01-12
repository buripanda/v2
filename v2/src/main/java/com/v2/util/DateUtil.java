package com.v2.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
  
  private static final String FORMAT = "yyyy/MM/dd HH:mm";
  
  /**
   * 文字列を日付に変換
   * @return
   */
  public static LocalDateTime changeStringDate (String str) throws Exception {
    LocalDateTime ldt = LocalDateTime.parse
        (str, DateTimeFormatter.ofPattern(FORMAT));
    return ldt;
  }

  /**
   * 日付を文字列に変換
   * @return
   */
  public static String changeDateString (LocalDateTime ldt) {
    String ret = "";
    try {
      DateTimeFormatter dtf = DateTimeFormatter.ofPattern(FORMAT);
      ret = dtf.format(ldt);
    } catch (Exception e) {
      
    }
    return ret;
  }

  /**
   * 開始日時から終了日時を算出
   * @param startDate
   * @return
   */
  public static LocalDateTime getEndDate(String str, int cnt) {
    
    LocalDateTime ldt = LocalDateTime.parse
        (str, DateTimeFormatter.ofPattern(FORMAT));
    LocalDateTime ldtp = ldt.plusMinutes(cnt * 15);
    return ldtp;
    
  }

}
