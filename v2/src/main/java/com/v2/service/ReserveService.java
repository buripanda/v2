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

import com.v2.bean.PurchaseHist;
import com.v2.bean.ReserveHist;
import com.v2.dao.PurchaseHistDao;
import com.v2.dao.ReserveHistDao;
import com.v2.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ReserveService {
  
  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  ReserveHistDao reserveHistDao;

  @Autowired
  PurchaseHistDao purchaseHistDao;

  /**
   * 日程を予約する
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
  public void doReserve(int id, int pid, int ticketCnt, int price, String reserveDate,
      JdbcTemplate jdbcTemplate) throws Exception {
    
    // 予約履歴シーケンス取得
    int rseq = reserveHistDao.getSequenceReserveId(jdbcTemplate);
    
    // 予約履歴マッピング
    ReserveHist reserve = new ReserveHist();
    reserve.reserveId = rseq;
    reserve.buyerId = id;
    reserve.sellerId = pid;
    reserve.quantity = ticketCnt;
    reserve.price = price;
    reserve.amount = ticketCnt * price;
    reserve.reserveStartDate = DateUtil.changeStringDate(reserveDate);
    reserve.reserveEndDate = DateUtil.getEndDate(reserveDate, ticketCnt);
    reserve.buyerRate = new BigDecimal(0.0);
    reserve.sellerRate = new BigDecimal(0.0);
    System.out.println(reserve.toString());
    
    // 予約履歴を登録する
    reserveHistDao.insertReserveHist(reserve, jdbcTemplate);

    // 購入履歴シーケンス取得
    int pseq = purchaseHistDao.getSequencePurchaseId(jdbcTemplate);

    // 購入履歴マッピング
    PurchaseHist purchase = new PurchaseHist();
    purchase.purchaseId = pseq;
    purchase.buyerId = id;
    purchase.sellerId = pid;
    purchase.purchaseKbn = 1;
    purchase.quantity = ticketCnt;
    purchase.amount = ticketCnt * price;
    System.out.println(purchase.toString());
    
    

    // 購入履歴を登録する
    purchaseHistDao.insertPurchaseHist(purchase, jdbcTemplate);

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