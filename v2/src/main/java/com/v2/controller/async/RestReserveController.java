package com.v2.controller.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.ReserveService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Scope("request")
public class RestReserveController extends AbstractController {
 
  @Autowired
  ReserveService reserveService;
  
  Logger logger = LoggerFactory.getLogger(RestMessageController.class);
  
  private final JdbcTemplate jdbcTemplate;
  public RestReserveController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * 日程予約処理
   * @return
   */
  @PostMapping("/restReserve")
  public String restReserve(
      @RequestParam("pid") int pid, 
      @RequestParam("ticket_cnt") int ticketCnt, 
      @RequestParam("price") int price,
      @RequestParam("reserve_date") String reserveDate) {    
    
    // セッションからログインID取得
    if (!super.isLogin())
      return "日程の予約に失敗しました";    
    User user = super.getSessionBean();
    int id = user.id;

    // 各値チェック
    if (!reserveService.doCheck(ticketCnt, reserveDate)) {
      return "日程の予約に失敗しました";
    }
    
    try {    
      //日程予約処理
      reserveService.doReserve(id, pid, ticketCnt, price, reserveDate, jdbcTemplate);
    } catch (Exception e) {
      e.printStackTrace();
      return "エラーが発生しました";
    }
    return "ok";  
  }
}
