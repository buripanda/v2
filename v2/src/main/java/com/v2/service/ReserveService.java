package com.v2.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ReserveService {
  
  private final String FORMAT = "yyyy/MM/dd HH:mm";

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
    reserve.reserveStartDate = this.changeStringDate(reserveDate);
    reserve.reserveEndDate = this.getEndDate(reserveDate, ticketCnt);
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
  /**
   * 文字列を日付に変換
   * @return
   */
  private LocalDateTime changeStringDate (String str) throws Exception {
    LocalDateTime ldt = LocalDateTime.parse
        (str, DateTimeFormatter.ofPattern(FORMAT));
    return ldt;
  }
  
  /**
   * 開始日時から終了日時を算出
   * @param startDate
   * @return
   */
  private LocalDateTime getEndDate(String str, int cnt) {
    
    LocalDateTime ldt = LocalDateTime.parse
        (str, DateTimeFormatter.ofPattern(FORMAT));
    LocalDateTime ldtp = ldt.plusMinutes(cnt * 15);
    return ldtp;
    
  }
}