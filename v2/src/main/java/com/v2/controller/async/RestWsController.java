package com.v2.controller.async;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v2.bean.WsMessage;
import com.v2.service.LoginService;
import com.v2.service.MessageService;

@Controller
public class RestWsController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	MessageService messageService;
	
	@Autowired
	LoginService loginService;
	
	private final JdbcTemplate jdbcTemplate;
	public RestWsController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * チャットのメッセージ送信を待ち受け、配信する
	 * @param id
	 * @param message
	 * @throws Exception
	 */
	@MessageMapping("/chat")
	public void chat(WsMessage wsMessage) throws Exception {
  /*public void greeting(@DestinationVariable String sendId, WsMessage message) throws Exception {*/

		System.out.println("メッセージ送信先：" + wsMessage.sendId + "　送信元：" + wsMessage.distId);

		try {
			// メッセージを登録する
			messageService.registMessage(Integer.parseInt(wsMessage.distId), Integer.parseInt(wsMessage.sendId), wsMessage.message ,jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// JSON文字列に変換
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonStr = objectMapper.writeValueAsString(wsMessage);
		System.out.println("JSON文字列：" + jsonStr);
		
		// 購読している人へメッセージを送る
		simpMessagingTemplate.convertAndSend("/topic/greetings", jsonStr);

	}

	/**
	 * ログインステータスをONにする
	 * @param id
	 * @param message
	 * @throws Exception
	 */
	@MessageMapping("/login")
	public void login(WsMessage wsMessage) throws Exception {
  /*public void greeting(@DestinationVariable String sendId, WsMessage message) throws Exception {*/

		System.out.println("ログインID：" + wsMessage.distId);

		try {
			// ログインステータスをONにする
			loginService.onlineStatusOn(Integer.parseInt(wsMessage.distId), jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// JSON文字列に変換
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonStr = objectMapper.writeValueAsString(wsMessage);
		System.out.println("JSON文字列：" + jsonStr);
		
		// 購読している人へメッセージを送る
		simpMessagingTemplate.convertAndSend("/topic/greetings", jsonStr);

	}

}
