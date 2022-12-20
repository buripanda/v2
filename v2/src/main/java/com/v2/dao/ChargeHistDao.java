package com.v2.dao;

import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.v2.bean.ChargeHist;

@Repository
public class ChargeHistDao {
	
  
  /**
   * 課金登録
   * @param user
   * @param jdbcTemplate
   */
  public void inserChargeHist(ChargeHist charge,  JdbcTemplate jdbcTemplate) throws Exception {
  	
		// プロフィール新規登録
		jdbcTemplate.update(
		    "INSERT INTO T_CHARGE_HIST (CHARGE_ID, FROM_ID , TO_ID, CHARGE_DATE, CHARGE_KBN, QUANTITY, "
		    + " AMOUNT, DELETE_FLG, REGIST_DATE, UPDATE_DATE) " 
		    +  "VALUES (?, ?, ?, current_timestamp, ?, ?, ?, 0, current_timestamp, current_timestamp)",
		        charge.chargeId, charge.fromId, charge.toId, charge.chargeKbn, charge.quantity, charge.amount);

  }
  
  /**
   * シーケンス取得
   * @param jdbcTemplate
   * @return
   */
  public int getSequenceChargeId(JdbcTemplate jdbcTemplate) throws Exception {
    
    // ユーザIDのシーケンス取得
    return jdbcTemplate.queryForObject("SELECT NEXT VALUE FOR CHARGE_ID_SEQ", Integer.class);
  
  }

  /**
	 * 課金情報をセットする
	 * @param data
	 * @return
	 */
	private ChargeHist setChargeHist(Map<String, Object> data) {
		
		ChargeHist charge = new ChargeHist();
		return charge;
		
	}

}
