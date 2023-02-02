package com.v2.dao;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.v2.bean.Recruit;

@Repository
public class RecruitDao {
  
  /**
  * 募集メッセージを取得する
  * @param id
  * @param jdbcTemplate
  * @return
  * @throws Exception
  */
 public Recruit selectRecruitMessage(int id, JdbcTemplate jdbcTemplate) throws Exception {
   
	 String sql = 
			 "SELECT T1.ID, T1.RECRUIT_START_DATE, T1.RECRUIT_END_DATE, T1.MESSAGE, T1.STOP_FLG, T1.NOW_FLG, T1.DELETE_FLG, "
			 + "T2.USER_NAME, T2.PRICE"
			 + " FROM T_RECRUIT T1 INNER JOIN T_USER T2 ON T1.ID = T2.ID AND T2.DELETE_FLG = 0 "
			 + "WHERE T1.ID = ? AND T1.STOP_FLG = 0 AND T1.DELETE_FLG =  0 AND T1.REGIST_DATE > CURRENT_TIMESTAMP - 3600";
	 System.out.println(sql);
	 List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql, id); 
   
   if (dataList.size() == 1) 
     return this.setRecruit(dataList.get(0));
   return new Recruit();

 }

	/**
	 * 募集する
	 * @param id
	 * @param pid
	 * @param message
	 * @param jdbcTemplate
	 * @throws Exception
	 */
	public void insertRecruit(Recruit recruit,
	    JdbcTemplate jdbcTemplate) throws Exception {
		
		// 募集登録
		String sql = 
				"INSERT INTO T_RECRUIT ("
					    +" ID, RECRUIT_START_DATE ,RECRUIT_END_DATE, MESSAGE, STOP_FLG, NOW_FLG, DELETE_FLG,"
					    + " REGIST_DATE, UPDATE_DATE) "
							+ " VALUES (?, ?, ?, ?, 0, ?, 0, current_timestamp, current_timestamp)";
		 System.out.println(sql);
		jdbcTemplate.update(sql,
				recruit.id, recruit.recruitStartDate, recruit.recruitEndDate, recruit.message, recruit.nowFlg);

	}
	
  /**
   * 出品者が購入者の評価をする
   * @param emal
   * @param password
   * @param jdbcTemplate
   * @return
   */
  public int updateRateBuyer(int reserveId, int rate, String comment, JdbcTemplate jdbcTemplate) throws Exception {
    
    int cnt = jdbcTemplate.update(
        "UPDATE T_RESERVE_HIST SET " +
        "SELLER_RATE=? ,SELLER_COMMENT=?, SELLER_FLG=1, UPDATE_DATE=current_timestamp " +
        "WHERE  RESERVE_ID=?",
        rate, comment, reserveId);    
    return cnt;
  
  }
  /**
   * 購入者が出品者の評価をする
   * @param emal
   * @param password
   * @param jdbcTemplate
   * @return
   */
  public int updateRateSeller(int reserveId, int rate, String comment, JdbcTemplate jdbcTemplate) throws Exception {
    
    int cnt = jdbcTemplate.update(
        "UPDATE T_RESERVE_HIST SET " +
        "BUYER_RATE=? ,BUYER_COMMENT=?, BUYER_FLG=1, UPDATE_DATE=current_timestamp " +
        "WHERE  RESERVE_ID=?",
        rate, comment, reserveId);    
    return cnt;
  
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
  private Recruit setRecruit(Map<String, Object> data) {
    
	  Recruit recruit = new Recruit();
	  recruit.id = (int)data.get("ID");
	  recruit.recruitStartDate =  ((Timestamp)data.get("RECRUIT_START_DATE")).toLocalDateTime();
	  recruit.recruitEndDate = ((Timestamp)data.get("RECRUIT_END_DATE")).toLocalDateTime();
	  if (data.get("MESSAGE") != null) 
		  recruit.message = (String)data.get("MESSAGE");
	  recruit.stopFlg = (int)data.get("STOP_FLG");
	  recruit.nowFlg = (int)data.get("NOW_FLG");
	  recruit.id = (int)data.get("ID");
	  recruit.id = (int)data.get("ID");
	  recruit.id = (int)data.get("ID");
	  recruit.deleteFlg = (String)data.get("DELETE_FLG");
	  recruit.registDate = (Date)data.get("REGIST_DATE");
	  recruit.updateDate = (Date)data.get("UPDATE_DATE");
	  return recruit;
    
  }

}
