package com.v2.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
  
  /**
   * 募集一覧を表示する
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public List<Recruit> viewListRecruit(JdbcTemplate jdbcTemplate) throws Exception {
    
    // 募集一覧（今すぐ遊べる）を検索
    List list = recruitDao.selectRecruitListOnTime(jdbcTemplate);
  
    // 募集一覧（予約）を検索
    list.addAll(recruitDao.selectRecruitListReserve(jdbcTemplate));
    
    return list;

  }

  /**
   * 募集登録する
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public void registRecruit(Recruit param, JdbcTemplate jdbcTemplate) throws Exception {
    // 募集中かどうか検索する
    int cnt = recruitDao.selectRecruitCnt(param.id, jdbcTemplate);
    if (cnt > 0) {
      // 募集中データがあった場合は削除する
      recruitDao.updateRecruit(param, jdbcTemplate);
    }
    // 今すぐ遊ぶの場合は、時間を今～1時間にする
    if (param.nowFlg == 1) {
      param.recruitStartDate = LocalDateTime.now();
      param.recruitEndDate = LocalDateTime.now().plusHours(1);
    }
    // 募集登録する
    recruitDao.insertRecruit(param, jdbcTemplate);
  }
  /**
   * 募集を終了する
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public void registStop(Recruit param, JdbcTemplate jdbcTemplate) throws Exception {
    // 募集中データを削除する
    recruitDao.updateRecruit(param, jdbcTemplate);
  }
  /**
   * パラメータチェック
   * @param user
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public boolean isParam(Recruit param) throws Exception {
    
    if (param.recruitStartDate == null) {
      param.errorMessage = "募集開始日時が入力されていません";
      return false;
    }
    if (param.recruitEndDate == null) {
      param.errorMessage = "募集終了日時が入力されていません";
      return false;
    }
    if (!StringUtils.hasLength(param.message)) {
      param.errorMessage = "募集内容が入力されていません";
      return false;
    }
    if (param.recruitStartDate.isAfter(param.recruitEndDate)) {
      param.errorMessage = "募集終了日時が募集開始日時前になっています";
      return false;      
    }
    return true;
    
  }
}