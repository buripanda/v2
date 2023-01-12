package com.v2.controller.async;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.v2.bean.Chat;
import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.HtmlService;
import com.v2.service.LoginService;
import com.v2.service.MessageService;
import com.v2.service.ProfileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Scope("request")
public class RestMessageController extends AbstractController {
	
	@Autowired
	HtmlService htmlService;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	ProfileService profileService;
	
	Logger logger = LoggerFactory.getLogger(RestMessageController.class);
	
	private final JdbcTemplate jdbcTemplate;
	public RestMessageController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * メッセージ取得
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/getMessageData")
	public String getMessageData(@RequestParam("sendId") int sendId, @RequestParam("distId") int distId) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "";
		
		// チャットリスト取得
		List<Chat> chatList = new ArrayList<>();
		String ret = null;
		try {
			chatList = messageService.getMessageList(distId, sendId, jdbcTemplate);
			ret = htmlService.getChatList(distId, chatList);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		
		//セッションに選択ユーザを保持
		// 選択したチャット画面の相手のpidを保持
		super.setSessionBeanInt("cpid", sendId);
		
		logger.info(ret);
		return ret;
		
	}

  /**
   * プロフィール情報取得
   * @param id
   * @param modelMap
   * @return
   */
  @PostMapping("/getChatProfileData")
  public String getChatProfileData(@RequestParam("selectId") int selectId) {
    
    // セッションからログインID取得
    if (!super.isLogin())
      return "";
    
    // プロフィール取得
    User user = new User();
    String ret = null;
    try {
      user = profileService.getProfile(selectId, jdbcTemplate);
      ret = htmlService.getProfile(user);
    } catch (Exception e) {
      e.printStackTrace();
      return "error";
    }
    logger.info(ret);
    return ret;
    
  }
}
