package com.v2.bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import com.v2.util.DateUtil;

import lombok.Data;

@Data
public class ReserveHist {

  public int reserveId;  
  public int buyerId;
  public int sellerId;
  public int quantity;
  public int price;
  public int amount;
  public LocalDateTime reserveStartDate;
  public LocalDateTime reserveEndDate;
  public String buyerComment;
  public BigDecimal buyerRate;
  public String sellerComment;
  public BigDecimal sellerRate;
  public String deleteFlg;
  /** 更新日時 */
  public Date updateDate;
  /** 登録日時 */
  public Date registDate;
  public int id;
  public String userName;
  public String imageFile;
  public int buysellFlg;
  
  public String getReserveStartDate() {
    return DateUtil.changeDateString(reserveStartDate);
  }
  public String getReserveEndDate() {
    return DateUtil.changeDateString(reserveEndDate);
  }
}
