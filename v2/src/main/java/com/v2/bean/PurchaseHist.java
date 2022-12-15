package com.v2.bean;

import java.util.Date;

import lombok.Data;

@Data
public class PurchaseHist {

  public int purchaseId;
  public int buyerId;
  public int sellerId;
  public Date purchaseDate;
  public int purchaseKbn;
  public int quantity;
  public int amount;
  public String deleteFlg;
  /** 更新日時 */
  public Date updateDate;
  /** 登録日時 */
  public Date registDate;

}
