package com.v2.controller.async;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.v2.bean.Chat;
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
	@GetMapping("/getPartnerData")
	public String getPartnerData(@RequestParam("partnerId") int partnerId) {
		
		// セッションからログインID取得
		//if (!super.isLogin())
		//	return "redirect:/login";		
		//super.getSessionBean();
		//int id = super.getSessionBean().id;
		
		
		return "テストテスト";
		
	}

	/**
	 * メッセージ画面（パートナーデータ読み込み）
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/getPartnerData")
	public String getPartnerDataPost(@RequestParam("partnerId") int partnerId) {
		
		// セッションからログインID取得
		/*
		if (!super.isLogin())
			return "redirect:/login";
		
		super.getSessionBean();
		int id = super.getSessionBean().id;
		*/
		// チャットリスト取得
		List<Chat> chatList = new ArrayList<>();
		String ret = null;
		try {
			chatList = messageService.doView(8, 12, jdbcTemplate);
			ret = htmlService.getChatList(8, chatList);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		
		logger.info(ret);
		System.out.println(ret);
		return ret;
		
	}
}
