package com.v2.bean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

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
}
