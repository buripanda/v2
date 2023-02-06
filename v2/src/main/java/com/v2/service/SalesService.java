package com.v2.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.v2.bean.Sales;
import com.v2.bean.User;
import com.v2.dao.SalesDao;
import com.v2.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SalesService {
  
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  SalesDao salesDao;
  
  @Autowired
  UserDao userDao;

  /**
   * 売上一覧を取得
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public List<Sales> getSalesList(int id, JdbcTemplate jdbcTemplate) throws Exception {
	  // 売上一覧を検索する
	 return salesDao.selectSalesList(id, jdbcTemplate);
  }
  /**
   * 売上金額を取得
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public User getSalesSum(int id, JdbcTemplate jdbcTemplate) throws Exception {
	  //売上金額を取得する
	  return userDao.selectUser(id, jdbcTemplate);
  }
}