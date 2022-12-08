package com.v2.controller.sync;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.v2.bean.Chat;
import com.v2.bean.SessionBean;
import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.MessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
public class MessageController extends AbstractController {
	
	@Autowired
	MessageService messageService;
	
	private final JdbcTemplate jdbcTemplate;

	public MessageController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	
	/**
	 * メッセージ画面（パートナー一覧）
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/messagePartnerList")
	public String messagePartnerList(@RequestParam(name="pid", defaultValue = "0") int pid, ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";		
		super.getSessionBean();
		int id = super.getSessionBean().id;
		
		List<User> partnerList = new ArrayList<>();
		try {
			//pidがある場合はパートナー履歴に追加（チャットするから来た）
			if (pid != 0)
				messageService.registPartner(id, pid, jdbcTemplate);
		
			// パートナー一覧検索
			partnerList = messageService.getPartnerList(id, jdbcTemplate);
			modelMap.addAttribute("partnerList", partnerList);
			
			List<Chat> chatList = messageService.getMessageList(id, pid, jdbcTemplate);
			modelMap.addAttribute("chatList", chatList);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "message";
		
	}



	/**
	 * チャット画面（送信）
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/chat/soshin")
	public String signupPost(@RequestParam("message") String message,
			ModelMap modelMap) {
		
		// ログインしてない場合はトップ画面へ
		if (!this.isLogin())
			return "redirect:/";

		// セッションからidを取得
		SessionBean bean = this.getSessionBean();

		// パートナー一覧検索
		List<Chat> chatList = new ArrayList<>();
		try {
			messageService.doChat(bean.id, bean.pid, message, jdbcTemplate);
			chatList = messageService.getMessageList(bean.id, bean.pid, jdbcTemplate);
			modelMap.addAttribute("chatList", chatList);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

		return "chat";
	}


}
