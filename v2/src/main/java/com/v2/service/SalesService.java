package com.v2.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.v2.bean.Sales;
import com.v2.bean.SalesHist;
import com.v2.dao.SalesDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SalesService {
  
  private final Logger logger = LoggerFactory.getLogger(getClass());
  private final SalesDao salesDao;
  private final JdbcTemplate jdbcTemplate;
  
  public SalesService(SalesDao salesDao, JdbcTemplate jdbcTemplate) {
	  this.salesDao = salesDao;
	  this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * 売上一覧を取得
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public List<SalesHist> getSalesList(int id) throws Exception {
	  // 売上一覧を検索する
	 return salesDao.selectSalesList(id, jdbcTemplate);
  }
  /**
   * 売上金額を取得
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public Sales getSalesSum(int id) throws Exception {
	  //売上金額を取得する
	  return salesDao.selectSales(id, jdbcTemplate);
  }
}