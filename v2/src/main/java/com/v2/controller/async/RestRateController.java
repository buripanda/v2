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
import com.v2.service.HtmlService;
import com.v2.service.RateService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Scope("request")
public class RestRateController extends AbstractController {
 
  @Autowired
  RateService rateService;
  
  @Autowired
  HtmlService htmlService;

  Logger logger = LoggerFactory.getLogger(RestMessageController.class);
  
  private final JdbcTemplate jdbcTemplate;
  public RestRateController(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  /**
   * 評価処理
   * @return
   */
  @PostMapping("/restRate")
  public String restRate(
      @RequestParam("pid") int pid,
      @RequestParam("reserve_id") int reserveId,
      @RequestParam("buysell_flg") int buysellFlg,
      @RequestParam("star") int rate, 
      @RequestParam(value="comment", required=false) String comment) {
    
    // セッションからログインID取得
    if (!super.isLogin())
      return "評価に失敗しました";    
    User user = super.getSessionBean();
    int id = user.id;

    // 各値チェック
    
    try {    
      //評価処理
      rateService.doRate(id, pid, rate, comment, buysellFlg, reserveId, jdbcTemplate);
    } catch (Exception e) {
      e.printStackTrace();
      return "エラーが発生しました";
    }
    return "ok";  
  }
}
