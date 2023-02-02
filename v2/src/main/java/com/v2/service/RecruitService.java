package com.v2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.v2.bean.Recruit;
import com.v2.dao.RecruitDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class RecruitService {
  
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  RecruitDao recruitDao;
  /**
   * 募集管理画面を表示する
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public Recruit viewRecruit(int id, JdbcTemplate jdbcTemplate) throws Exception {
    // 募集メッセージを検索する
	  return recruitDao.selectRecruitMessage(id, jdbcTemplate);
  }
}