package com.v2.dao;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.v2.bean.ReserveHist;

@Repository
public class ReserveHistDao {
  
  /**
  * 終了してない特定の相手の予約一覧を取得する（購入者）
  * @param id
  * @param jdbcTemplate
  * @return
  * @throws Exception
  */
 public List<ReserveHist> selectReserveListBuy(int id, int pid, JdbcTemplate jdbcTemplate) throws Exception {
   
   List<Map<String, Object>> dataList = jdbcTemplate.queryForList(
       "SELECT"
       + " T1.RESERVE_ID, T1.BUYER_ID ,T1.SELLER_ID, T1.QUANTITY, T1.PRICE, "
       + " T1.AMOUNT, T1.RESERVE_START_DATE, T1.RESERVE_END_DATE, T1.BUYER_COMMENT, "
       + " T1.BUYER_RATE, T1.SELLER_COMMENT, T1.SELLER_RATE, T1.DELETE_FLG, "
       + " T1.REGIST_DATE, T1.UPDATE_DATE, T2.ID, T2.IMAGE_PATH, T2.USER_NAME "
       + "FROM"
       + " T_RESERVE_HIST T1"
       + " INNER JOIN T_USER T2"
       + " ON T1.SELLER_ID = T2.ID"
       + " WHERE"
       + "  T1.BUYER_ID = ? "
       + "  AND T1.SELLER_ID = ? "
       + "  AND T1.BUYER_FLG = 0 "        
       + "  AND T1.DELETE_FLG = 0 "        
       + "ORDER BY"
       + "  T1.RESERVE_START_DATE", 
       id, pid); 
   
   List<ReserveHist> reserveList = new ArrayList<>();
   for (Map<String, Object> data : dataList) {
     reserveList.add(this.setReserveHist(data));
   }
   return reserveList;

 }
   /**
   * 終了してない予約一覧を取得する（購入者）
   * @param id
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public List<ReserveHist> selectReserveListBuyAll(int id, JdbcTemplate jdbcTemplate) throws Exception {
    
    List<Map<String, Object>> dataList = jdbcTemplate.queryForList(
        "SELECT"
        + " T1.RESERVE_ID, T1.BUYER_ID ,T1.SELLER_ID, T1.QUANTITY, T1.PRICE, "
        + " T1.AMOUNT, T1.RESERVE_START_DATE, T1.RESERVE_END_DATE, T1.BUYER_COMMENT, "
        + " T1.BUYER_RATE, T1.SELLER_COMMENT, T1.SELLER_RATE, T1.DELETE_FLG, "
        + " T1.REGIST_DATE, T1.UPDATE_DATE, T2.ID, T2.IMAGE_PATH, T2.USER_NAME "
        + "FROM"
        + " T_RESERVE_HIST T1"
        + " INNER JOIN T_USER T2"
        + " ON T1.SELLER_ID = T2.ID"
        + " WHERE"
        + "  T1.BUYER_ID = ? "
        + "  AND T1.BUYER_FLG = 0 "        
        + "  AND T1.DELETE_FLG = 0 "        
        + "ORDER BY"
        + "  T1.RESERVE_START_DATE", 
        id); 
    
    List<ReserveHist> reserveList = new ArrayList<>();
    for (Map<String, Object> data : dataList) {
      reserveList.add(this.setReserveHist(data));
    }
    return reserveList;
  
  }
   /**
   * 終了した特定の相手の予約一覧を取得する（購入者）
   * @param id
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public List<ReserveHist> selectReserveListBuyComp(int id, int pid, JdbcTemplate jdbcTemplate) throws Exception {
    
    List<Map<String, Object>> dataList = jdbcTemplate.queryForList(
        "SELECT"
        + " T1.RESERVE_ID, T1.BUYER_ID ,T1.SELLER_ID, T1.QUANTITY, T1.PRICE, "
        + " T1.AMOUNT, T1.RESERVE_START_DATE, T1.RESERVE_END_DATE, T1.BUYER_COMMENT, "
        + " T1.BUYER_RATE, T1.SELLER_COMMENT, T1.SELLER_RATE, T1.DELETE_FLG, "
        + " T1.REGIST_DATE, T1.UPDATE_DATE, T2.ID, T2.IMAGE_PATH, T2.USER_NAME "
        + "FROM"
        + " T_RESERVE_HIST T1"
        + " INNER JOIN T_USER T2"
        + " ON T1.SELLER_ID = T2.ID"
        + " WHERE"
        + "  T1.BUYER_ID = ? "
        + "  AND T1.SELLER_ID = ? "
        + "  AND T1.SELLER_FLG = 1 "        
        + "  AND T1.DELETE_FLG = 0 "        
        + "ORDER BY"
        + "  T1.RESERVE_START_DATE", 
        id, pid); 
    
    List<ReserveHist> reserveList = new ArrayList<>();
    for (Map<String, Object> data : dataList) {
      reserveList.add(this.setReserveHist(data));
    }
    return reserveList;
  
  }

    /**
    * 終了した予約一覧を取得する（購入者）
    * @param id
    * @param jdbcTemplate
    * @return
    * @throws Exception
    */
   public List<ReserveHist> selectReserveListBuyCompAll(int id, JdbcTemplate jdbcTemplate) throws Exception {
     
     List<Map<String, Object>> dataList = jdbcTemplate.queryForList(
         "SELECT"
         + " T1.RESERVE_ID, T1.BUYER_ID ,T1.SELLER_ID, T1.QUANTITY, T1.PRICE, "
         + " T1.AMOUNT, T1.RESERVE_START_DATE, T1.RESERVE_END_DATE, T1.BUYER_COMMENT, "
         + " T1.BUYER_RATE, T1.SELLER_COMMENT, T1.SELLER_RATE, T1.DELETE_FLG, "
         + " T1.REGIST_DATE, T1.UPDATE_DATE, T2.ID, T2.IMAGE_PATH, T2.USER_NAME "
         + "FROM"
         + " T_RESERVE_HIST T1"
         + " INNER JOIN T_USER T2"
         + " ON T1.SELLER_ID = T2.ID"
         + " WHERE"
         + "  T1.BUYER_ID = ? "
         + "  AND T1.SELLER_FLG = 1 "        
         + "  AND T1.DELETE_FLG = 0 "        
         + "ORDER BY"
         + "  T1.RESERVE_START_DATE", 
         id); 
     
     List<ReserveHist> reserveList = new ArrayList<>();
     for (Map<String, Object> data : dataList) {
       reserveList.add(this.setReserveHist(data));
     }
     return reserveList;
   
   }
  /**
   * 終了してない特定の相手の予約一覧を取得する（出品者）
   * @param id
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public List<ReserveHist> selectReserveListSel(int id, int pid, JdbcTemplate jdbcTemplate) throws Exception {
    
    List<Map<String, Object>> dataList = jdbcTemplate.queryForList(
        "SELECT"
        + " T1.RESERVE_ID, T1.BUYER_ID ,T1.SELLER_ID, T1.QUANTITY, T1.PRICE, "
        + " T1.AMOUNT, T1.RESERVE_START_DATE, T1.RESERVE_END_DATE, T1.BUYER_COMMENT, "
        + " T1.BUYER_RATE, T1.SELLER_COMMENT, T1.SELLER_RATE, T1.DELETE_FLG, "
        + " T1.REGIST_DATE, T1.UPDATE_DATE, T2.ID, T2.IMAGE_PATH, T2.USER_NAME "
        + " FROM"
        + " T_RESERVE_HIST T1"
        + " INNER JOIN T_USER T2"
        + " ON T1.BUYER_ID = T2.ID"
        + " WHERE"
        + "  T1.BUYER_ID = ? "
        + "  AND T1.SELLER_ID = ? "
        + "  AND T1.SELLER_FLG = 0 "        
        + "  AND T1.DELETE_FLG = 0 "        
        + "ORDER BY"
        + "  T1.RESERVE_START_DATE", 
        pid, id); 
    
    List<ReserveHist> reserveList = new ArrayList<>();
    for (Map<String, Object> data : dataList) {
      reserveList.add(this.setReserveHist(data));
    }
    return reserveList;
  
  }


	/**
	 * 日程予約する
	 * @param id
	 * @param pid
	 * @param message
	 * @param jdbcTemplate
	 * @throws Exception
	 */
	public void insertReserveHist(ReserveHist reserve,
	    JdbcTemplate jdbcTemplate) throws Exception {
		
		// 日程予約登録
		jdbcTemplate.update(
				"INSERT INTO T_RESERVE_HIST ("
		    +" RESERVE_ID, BUYER_ID ,SELLER_ID, QUANTITY, PRICE, AMOUNT, RESERVE_START_DATE,"
		    + " RESERVE_END_DATE, BUYER_COMMENT, BUYER_RATE, SELLER_COMMENT, SELLER_RATE,"
		    + " DELETE_FLG, REGIST_DATE, UPDATE_DATE) "
				+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, 0, ?, 0, 0, current_timestamp, current_timestamp)",
				reserve.reserveId, reserve.buyerId, reserve.sellerId, reserve.quantity, 
				reserve.price, reserve.amount, reserve.reserveStartDate, reserve.reserveEndDate,
				reserve.buyerComment, reserve.sellerComment);

	}
	
  /**
   * シーケンス取得
   * @param jdbcTemplate
   * @return
   */
  public int getSequenceReserveId(JdbcTemplate jdbcTemplate) throws Exception {
    
    // ユーザIDのシーケンス取得
    return jdbcTemplate.queryForObject("SELECT NEXT VALUE FOR RESERVE_ID_SEQ", Integer.class);
  
  }

  /**
   * 予約履歴をセットする
   * @param data
   * @return
   */
  private ReserveHist setReserveHist(Map<String, Object> data) {
    
    ReserveHist reserve = new ReserveHist();
    reserve.reserveId = (int)data.get("RESERVE_ID");
    reserve.buyerId = (int)data.get("BUYER_ID");
    reserve.sellerId = (int)data.get("SELLER_ID");
    reserve.quantity = (int)data.get("QUANTITY");
    reserve.price = (int)data.get("PRICE");
    reserve.amount = (int)data.get("AMOUNT");
    if (data.get("RESERVE_START_DATE") != null) 
      reserve.reserveStartDate = ((Timestamp)data.get("RESERVE_START_DATE")).toLocalDateTime();
    if (data.get("RESERVE_END_DATE") != null) 
      reserve.reserveEndDate = ((Timestamp)data.get("RESERVE_END_DATE")).toLocalDateTime();
    if (data.get("BUYER_COMMENT") != null) 
      reserve.buyerComment = (String)data.get("BUYER_COMMENT");
    reserve.buyerRate = (BigDecimal)data.get("BUYER_RATE");
    if (data.get("SELLER_COMMENT") != null) 
      reserve.sellerComment = (String)data.get("SELLER_COMMENT");
    reserve.sellerRate = (BigDecimal)data.get("SELLER_RATE");
    reserve.deleteFlg = (String)data.get("DELETE_FLG");
    reserve.registDate = (Date)data.get("REGIST_DATE");
    reserve.updateDate = (Date)data.get("UPDATE_DATE");
    if (data.get("ID") != null) 
      reserve.id = (int)data.get("ID");
    if (data.get("USER_NAME") != null) 
      reserve.userName = (String)data.get("USER_NAME");
    if (data.get("IMAGE_PATH") != null) 
      reserve.imageFile = (String)data.get("IMAGE_PATH");
    return reserve;
    
  }

}
