package com.v2.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.v2.bean.Sales;
import com.v2.bean.SalesHist;

@Repository
public class SalesDao {
  

	/**
	 * 売上一覧取得
	 * @param id
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<SalesHist> selectSalesList(int id, JdbcTemplate jdbcTemplate) throws Exception {
	  
	  String sql = 
	      "SELECT T1.SALES_ID, T1.ID, T1.FROM_ID, T1.SALES_DATE, T1.SALES_KBN, T1.SALES_CONTENT, T1.AMOUNT,  T1.DELETE_FLG, "
	      + "T2.USER_NAME "
	      + " FROM T_SALES_HIST T1 INNER JOIN T_USER T2 ON T1.FROM_ID = T2.ID AND T2.DELETE_FLG = 0 "
	      + "WHERE T1.ID = ? AND T1.DELETE_FLG = 0 "
	      + " ORDER BY T1.SALES_DATE DESC";
	  System.out.println(sql);
	  List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql, id); 
	  
	  List<SalesHist> list = new ArrayList<>();
	  for (Map<String, Object> data : dataList) {
	    list.add(this.setParamSalesHist(data));
	  }
	  return list;
	
	}
	
	/**
	 * 売上管理情報取得（ID指定）
	 * @param id
	 * @return
	 */
	public Sales selectSales(int id, JdbcTemplate jdbcTemplate) throws Exception {
		String sql = "SELECT * FROM T_SALES WHERE ID = ?";
		 System.out.println(sql);
		List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql, id); // IDが一致するものを取得
		if (dataList.size() == 1) 
			return this.setParamSales(dataList.get(0));
		return new Sales();
	
	}

	
	  /**
	   * 売上管理新規登録
	   * @param user
	   * @param jdbcTemplate
	   */
	  public void insertSales(int id, JdbcTemplate jdbcTemplate) throws Exception {
		  
		  String sql =  
			        "INSERT INTO T_SALES (ID, REGIST_DATE, UPDATE_DATE) " +
				            "VALUES (?, current_timestamp, current_timestamp)";
		  System.out.println(sql);
		  jdbcTemplate.update(sql, id);

	  }

	  /**
	   * 売上金額更新
	   * @param user
	   * @param jdbcTemplate
	   */
	  public void updateSalesSalesSum(int id, int amount, JdbcTemplate jdbcTemplate) throws Exception {
		  
		  String sql =  
			        "UPDATE T_SALES SET SALES_SUM = SALES_SUM + ? WHERE ID = ?";
		  System.out.println(sql);
		  jdbcTemplate.update(sql, amount,  id);

	  }

	  /**
	   * 売上履歴登録
	   * @param user
	   * @param jdbcTemplate
	   */
	  public void insertSalesHist(SalesHist salesHist, JdbcTemplate jdbcTemplate) throws Exception {
		  
		  String sql =  
			        "INSERT INTO T_SALES_HIST (SALES_ID, ID, FROM_ID, SALES_DATE, SALES_KBN, SALES_CONTENT, "
			        + "AMOUNT, REGIST_DATE, UPDATE_DATE) " +
				            "VALUES (?, ?, ?, current_timestamp, ?, ?, ?, current_timestamp, current_timestamp)";
		  System.out.println(sql);
		  jdbcTemplate.update(sql, salesHist.salesId, salesHist.id, salesHist.fromId, salesHist.salesKbn, salesHist.salesContent, salesHist.amount);

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
   * 売上一覧情報を設定する
   * @param data
   * @return
   */
  private SalesHist setParamSalesHist(Map<String, Object> data) {
    
	  SalesHist sales = new SalesHist();
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
  /**
   * 売上一覧情報を設定する
   * @param data
   * @return
   */
  private Sales setParamSales(Map<String, Object> data) {
    
	  Sales sales = new Sales();
	  sales.id = (int)data.get("ID");
	  sales.salesSum = (int)data.get("SALES_SUM");
	  sales.salesCashSum = (int)data.get("SALES_CASH_SUM");
	  sales.nameSei  = (String)(Optional.ofNullable(data.get("NAME_SEI")).orElse(null));
	  sales.nameMei  = (String)(Optional.ofNullable(data.get("NAME_MEI")).orElse(null));
	  sales.nameSeiKana  = (String)(Optional.ofNullable(data.get("NAME_SEI_KANA")).orElse(null));
	  sales.nameMeiKana  = (String)(Optional.ofNullable(data.get("NAME_MEI_KANA")).orElse(null));
	  sales.bankName  = (String)(Optional.ofNullable(data.get("BANK_NAME")).orElse(null));
	  sales.bankBranchCd = (String)(Optional.ofNullable(data.get("BANK_BRANCH_CD")).orElse(null));
	  sales.bankKnd = (int)data.get("BANK_KND");
	  sales.kozaNo  = (String)(Optional.ofNullable(data.get("KOZA_NO")).orElse(null));
	  sales.kozaName  = (String)(Optional.ofNullable(data.get("KOZA_NAME")).orElse(null));
	  sales.salesCashStatus = (int)data.get("SALES_CASH_STATUS");
	  sales.deleteFlg = (String)data.get("DELETE_FLG");
	  sales.registDate = (Date)data.get("REGIST_DATE");
	  sales.updateDate = (Date)data.get("UPDATE_DATE");
	  return sales;
    
  }

}
