package com.v2.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.v2.bean.ReserveHist;
import com.v2.bean.User;
import com.v2.dao.PurchaseHistDao;
import com.v2.dao.ReserveHistDao;
import com.v2.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class RateService {
  
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  ReserveHistDao reserveHistDao;

  @Autowired
  PurchaseHistDao purchaseHistDao;
  
  @Autowired
  UserDao userDao;

  /**
   * 評価する
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public void doRate(int id, int pid, int rate, String comment, int buysellFlg, int reserveId,
      JdbcTemplate jdbcTemplate) throws Exception {
     
    if (buysellFlg == 1) {
      // 購入者が出品者の評価をする
      ReserveHist reserveHist = reserveHistDao.selectReserveHist(reserveId, jdbcTemplate);
      User user = userDao.selectUser(pid, jdbcTemplate);
      reserveHistDao.updateRateSeller(reserveId, rate, comment, jdbcTemplate);

      // 評価総数はチケット枚数ｘ評価とする
      int rateSum = (reserveHist.quantity * rate) + user.rateSum;
      int orderSum = user.orderSum + reserveHist.quantity;
      int i = rateSum / orderSum;
      int j = rateSum % orderSum;
      int totalRate = i;
      if (j != 0)
    	  totalRate++;
      //出品者のトータル評価を更新する      
      userDao.updateRate(pid, BigDecimal.valueOf(totalRate), rateSum, orderSum, jdbcTemplate);
    }
    if (buysellFlg == 2) {
      // 出品者が購入者の評価をする
      reserveHistDao.updateRateBuyer(reserveId, rate, comment, jdbcTemplate);
      // 評価再計算
      User user = userDao.selectUser(pid, jdbcTemplate);
      ReserveHist reserveHist = reserveHistDao.selectReserveHist(reserveId, jdbcTemplate);
      int rateSumBuy = rate + user.rateSumBuy;
      int orderSumBuy = user.orderSumBuy + reserveHist.quantity;
      int i = rateSumBuy / orderSumBuy;
      int j = rateSumBuy % orderSumBuy;
      int rateBuy = i;
      if (j != 0)
        rateBuy++;
      //購入者のトータル評価を更新する      
      userDao.updateRateBuy(pid, BigDecimal.valueOf(rateBuy), rateSumBuy, orderSumBuy, jdbcTemplate);
    }    
  }
  
  /**
   * 終了してない特定の相手の予約一覧を取得する（購入者）
   * @param id
   * @param pid
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public List<ReserveHist> getReserveListBuy(int id, int pid, JdbcTemplate jdbcTemplate) throws Exception {
    
    // 予約一覧を取得する（購入者）
    return reserveHistDao.selectReserveListBuy(id, pid, jdbcTemplate);
    
  }

  /**
   * 終了してない予約一覧を取得する（購入者）
   * @param id
   * @param pid
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public List<ReserveHist> getReserveListBuyAll(int id, JdbcTemplate jdbcTemplate) throws Exception {
    
    // 予約一覧を取得する（購入者）
    return reserveHistDao.selectReserveListBuyAll(id, jdbcTemplate);
    
  }
  /**
   * 終了した予約一覧を取得する（購入者）
   * @param id
   * @param pid
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public List<ReserveHist> getReserveListBuyCompAll(int id, JdbcTemplate jdbcTemplate) throws Exception {
    
    // 予約一覧を取得する（購入者）
    return reserveHistDao.selectReserveListBuyCompAll(id, jdbcTemplate);
    
  }
  
  /**
   * 評価された一覧を取得する（出品者）
   * @param id
   * @param pid
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public List<ReserveHist> getReserveListRateSeller(int id, JdbcTemplate jdbcTemplate) throws Exception {
    
    // 評価された一覧を取得する（出品者）
    return reserveHistDao.selectReserveListSeller(id, jdbcTemplate);
    
  }

  /**
   * 各値をチェック
   * @param email
   * @param password
   * @param agree
   * @return
   */
  public boolean doCheck(int ticketCnt, String reserveDate) {

    // ticketCntチェック
    if (ticketCnt < 1)
      return false;
    // reserveDateチェック
    if (!StringUtils.hasLength(reserveDate))
      return false;
    return true;

  }

}