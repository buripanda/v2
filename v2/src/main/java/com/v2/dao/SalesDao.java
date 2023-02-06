package com.v2.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.v2.bean.Sales;

@Repository
public class SalesDao {
  

	/**
	 * 売上一覧取得
	 * @param id
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<Sales> selectSalesList(int id, JdbcTemplate jdbcTemplate) throws Exception {
	  
	  String sql = 
	      "SELECT T1.SALES_ID, T1.ID, T1.FROM_ID, T1.SALES_DATE, T1.SALES_KBN, T1.SALES_CONTENT, T1.AMOUNT,  T1.DELETE_FLG, "
	      + "T2.USER_NAME "
	      + " FROM T_SALES T1 INNER JOIN T_USER T2 ON T1.FROM_ID = T2.ID AND T2.DELETE_FLG = 0 "
	      + "WHERE T1.ID = ? AND T1.DELETE_FLG = 0 "
	      + " ORDER BY T1.SALES_DATE DESC";
	  System.out.println(sql);
	  List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql); 
	  
	  List<Sales> list = new ArrayList<>();
	  for (Map<String, Object> data : dataList) {
	    list.add(this.setParam(data));
	  }
	  return list;
	
	}
  /**
   * シーケンス取得
   * @param jdbcTemplate
   * @return
   */
  public int getSequenceSalesId(JdbcTemplate jdbcTemplate) throws Exception {
    
    // ユーザIDのシーケンス取得
    return jdbcTemplate.queryForObject("SELECT NEXT VALUE FOR SALES_ID_SEQ", Integer.class);
  
  }
  /**
   * 募集情報を設定する
   * @param data
   * @return
   */
  private Sales setParam(Map<String, Object> data) {
    
	  Sales sales = new Sales();
	  sales.salesId = (int)data.get("SALES_ID");
	  sales.id = (int)data.get("ID");
	  sales.fromId = (int)data.get("FROM_ID");
	  sales.salesDate =  ((Timestamp)data.get("SALES_DATE")).toLocalDateTime();
	  if (data.get("SALES_KBN") != null) 
		  sales.salesKbn = (int)data.get("SALES_KBN");
	  if (data.get("SALES_CONTENT") != null) 
		  sales.salesContent = (String)data.get("SALES_CONTENT");
	  sales.amount = (int)data.get("AMOUNT");
	  sales.userName = (String)data.get("USER_NAME");
	  sales.deleteFlg = (String)data.get("DELETE_FLG");
	  sales.registDate = (Date)data.get("REGIST_DATE");
	  sales.updateDate = (Date)data.get("UPDATE_DATE");
	  return sales;
    
  }

}
