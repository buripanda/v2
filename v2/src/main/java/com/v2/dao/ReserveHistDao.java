package com.v2.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.v2.bean.ReserveHist;

@Repository
public class ReserveHistDao {

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


}
