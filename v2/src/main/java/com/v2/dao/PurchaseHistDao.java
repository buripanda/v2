package com.v2.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.v2.bean.PurchaseHist;

@Repository
public class PurchaseHistDao {


  /**
   * 購入履歴登録する
   * @param id
   * @param pid
   * @param message
   * @param jdbcTemplate
   * @throws Exception
   */
  public void insertPurchaseHist(PurchaseHist purchase,
      JdbcTemplate jdbcTemplate) throws Exception {
    
    // 購入履歴登録
    jdbcTemplate.update(
        "INSERT INTO T_PURCHASE_HIST ("
        + "  PURCHASE_ID, BUYER_ID, SELLER_ID, PURCHASE_DATE, PURCHASE_KBN, QUANTITY, AMOUNT,"
        + "  DELETE_FLG, REGIST_DATE, UPDATE_DATE) "
        + "VALUES (?, ?, ?, current_timestamp, ?, ?, ?, 0, current_timestamp, current_timestamp)",
        purchase.purchaseId, purchase.buyerId, purchase.sellerId, 
        purchase.purchaseKbn, purchase.quantity, purchase.amount);

  }
  
  /**
   * シーケンス取得
   * @param jdbcTemplate
   * @return
   */
  public int getSequencePurchaseId(JdbcTemplate jdbcTemplate) throws Exception {
    
    // ユーザIDのシーケンス取得
    return jdbcTemplate.queryForObject("SELECT NEXT VALUE FOR PURCHASE_ID_SEQ", Integer.class);
  
  }

}
