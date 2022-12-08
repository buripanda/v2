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
import com.v2.bean.SessionBean;
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
	 * メッセージ画面（パートナーデータ読み込み）
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/getMessageData")
	public String getMessageData(@RequestParam("pid") int pid) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";
		
		SessionBean bean = super.getSessionBean();
		int id = bean.id;

		// チャットリスト取得
		List<Chat> chatList = new ArrayList<>();
		String ret = null;
		try {
			chatList = messageService.getMessageList(id, pid, jdbcTemplate);
			ret = htmlService.getChatList(id, chatList);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		
		// 選択したチャット画面の相手のpidを保持
		bean.cpid = pid;
		super.setSessionBean(bean);
		
		logger.info(ret);
		return ret;
		
	}

	/**
	 * メッセージ送信
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/sendMessageData")
	public String sendMessageData(@RequestParam("message") String message) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";
		SessionBean bean = super.getSessionBean();
		int id = bean.id;
		int cpid = bean.cpid;

		List<Chat> chatList = new ArrayList<>();
		String ret = null;
		try {
			// メッセージを登録して、チャットリスト取得
			chatList = messageService.registMessage(id, cpid, message, jdbcTemplate);
			ret = htmlService.getChatList(id, chatList);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		
		logger.info(ret);
		return ret;
		
	}

}
