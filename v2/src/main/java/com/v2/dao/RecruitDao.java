package com.v2.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
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
			 "SELECT T1.ID, T1.RECRUIT_START_DATE, T1.RECRUIT_END_DATE, T1.MESSAGE, T1.RECRUIT_FLG, T1.STOP_FLG, T1.NOW_FLG, T1.DELETE_FLG, "
			 + "T2.USER_NAME, T2.PRICE, T2.IMAGE_PATH "
			 + " FROM T_RECRUIT T1 INNER JOIN T_USER T2 ON T1.ID = T2.ID AND T2.DELETE_FLG = 0 "
			 + "WHERE T1.ID = ? AND T1.STOP_FLG = 0 AND T1.DELETE_FLG =  0 AND T1.REGIST_DATE > CURRENT_TIMESTAMP - 0.042";
	 System.out.println(sql);
	 List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql, id); 
   
   if (dataList.size() == 1) 
     return this.setRecruit(dataList.get(0));
   return new Recruit();

 }
/**
 * 募集一覧を取得する（今すぐ遊べる）
 * @param id
 * @param jdbcTemplate
 * @return
 * @throws Exception
 */
public List<Recruit> selectRecruitListOnTime(JdbcTemplate jdbcTemplate) throws Exception {
  
  String sql = 
      "SELECT T1.ID, T1.RECRUIT_START_DATE, T1.RECRUIT_END_DATE, T1.MESSAGE, T1.RECRUIT_FLG, T1.STOP_FLG, T1.NOW_FLG, T1.DELETE_FLG, "
      + "T2.USER_NAME, T2.PRICE, T2.IMAGE_PATH "
      + " FROM T_RECRUIT T1 INNER JOIN T_USER T2 ON T1.ID = T2.ID AND T2.DELETE_FLG = 0 "
      + "WHERE T1.STOP_FLG = 0 AND T1.DELETE_FLG = 0 AND T1.NOW_FLG = 1 AND T1.RECRUIT_START_DATE > CURRENT_TIMESTAMP - 0.042"
      + " ORDER BY T1.RECRUIT_START_DATE DESC";
  System.out.println(sql);
  List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql); 
  
  List<Recruit> list = new ArrayList<>();
  for (Map<String, Object> data : dataList) {
    list.add(this.setRecruit(data));
  }
  return list;

}
/**
 * 募集一覧を取得する（予約）
 * @param id
 * @param jdbcTemplate
 * @return
 * @throws Exception
 */
public List<Recruit> selectRecruitListReserve(JdbcTemplate jdbcTemplate) throws Exception {
  
  String sql = 
      "SELECT T1.ID, T1.RECRUIT_START_DATE, T1.RECRUIT_END_DATE, T1.MESSAGE, T1.RECRUIT_FLG, T1.STOP_FLG, T1.NOW_FLG, T1.DELETE_FLG, "
      + "T2.USER_NAME, T2.PRICE, T2.IMAGE_PATH "
      + " FROM T_RECRUIT T1 INNER JOIN T_USER T2 ON T1.ID = T2.ID AND T2.DELETE_FLG = 0 "
      + "WHERE T1.STOP_FLG = 0 AND T1.DELETE_FLG = 0 AND T1.NOW_FLG = 0 AND T1.RECRUIT_START_DATE > CURRENT_TIMESTAMP - 0.042"
      + " ORDER BY T1.RECRUIT_START_DATE";
  System.out.println(sql);
  List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql); 
  
  List<Recruit> list = new ArrayList<>();
  for (Map<String, Object> data : dataList) {
    list.add(this.setRecruit(data));
  }
  return list;

}
 /**
 * 募集情報が登録されているか確認する（ID指定）
 * @param id
 * @return
 */
public int selectRecruitCnt(int id, JdbcTemplate jdbcTemplate) throws Exception {
  
  String sql = 
      "SELECT COUNT(*) FROM "
      + " T_RECRUIT "
      + " WHERE"
      + " ID = ? "
      + " AND DELETE_FLG = 0";
  System.out.println(sql);
  int cnt = jdbcTemplate.queryForObject(sql, Integer.class, id); 
  return cnt;
  
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
	  
	  int seq = getSequenceRecruitId(jdbcTemplate);
		
		// 募集登録
		String sql = 
				"INSERT INTO T_RECRUIT ("
					    +" RECRUIT_ID, ID, RECRUIT_START_DATE ,RECRUIT_END_DATE, MESSAGE, RECRUIT_FLG, STOP_FLG, NOW_FLG, DELETE_FLG,"
					    + " REGIST_DATE, UPDATE_DATE) "
							+ " VALUES (?, ?, ?, ?, ?, ?, 0, ?, 0, current_timestamp, current_timestamp)";
		 System.out.println(sql);
		jdbcTemplate.update(sql,
		    seq, recruit.id, recruit.recruitStartDate, recruit.recruitEndDate, 
		    recruit.message, recruit.recruitFlg, recruit.nowFlg);

	}
	
  /**
   * 募集情報を削除する
   * @param emal
   * @param password
   * @param jdbcTemplate
   * @return
   */
  public int updateRecruit(Recruit recruit, JdbcTemplate jdbcTemplate) throws Exception {
    
    String sql = 
        "UPDATE T_RECRUIT SET " +
        "STOP_FLG = 1, DELETE_FLG = 1, UPDATE_DATE=current_timestamp " +
        "WHERE  ID = ? AND DELETE_FLG = 0";    
    System.out.println(sql);
    int cnt = jdbcTemplate.update(sql,recruit.id);    
    return cnt;
  
  }

  /**
   * シーケンス取得
   * @param jdbcTemplate
   * @return
   */
  public int getSequenceRecruitId(JdbcTemplate jdbcTemplate) throws Exception {
    
    // ユーザIDのシーケンス取得
    return jdbcTemplate.queryForObject("SELECT NEXT VALUE FOR RECRUIT_ID_SEQ", Integer.class);
  
  }
  /**
   * 募集情報を設定する
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
	  recruit.recruitFlg = (int)data.get("RECRUIT_FLG");
	  recruit.stopFlg = (int)data.get("STOP_FLG");
	  recruit.nowFlg = (int)data.get("NOW_FLG");
	  recruit.userName = (String)data.get("USER_NAME");
	  recruit.price = (int)data.get("PRICE");
	  recruit.imageFile = (String)data.get("IMAGE_PATH");
	  recruit.deleteFlg = (String)data.get("DELETE_FLG");
	  recruit.registDate = (Date)data.get("REGIST_DATE");
	  recruit.updateDate = (Date)data.get("UPDATE_DATE");
	  return recruit;
    
  }

}
