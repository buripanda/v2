package com.v2.controller.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.v2.bean.WsMessage;
import com.v2.service.MessageService;

@Controller
public class GreetingController {

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	@Autowired
	MessageService messageService;
	
	private final JdbcTemplate jdbcTemplate;
	public GreetingController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * チャットのメッセージ送信を待ち受け、配信する
	 * @param id
	 * @param message
	 * @throws Exception
	 */
	@MessageMapping("/chat")
	public void greeting(WsMessage wsMessage) throws Exception {
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
		simpMessagingTemplate.convertAndSend("/topic/greetings/" + wsMessage.sendId, jsonStr);

	}

}
