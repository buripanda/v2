package com.v2.controller.sync;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.v2.bean.Chat;
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
	@GetMapping("/messagePartnerList")
	public String messagePartnerListG(ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";		
		int cpid = super.getSessionBeanInt("cpid");	
		return messagePartnerList(cpid, modelMap);
	
	}
	
	/**
	 * メッセージ画面（パートナー一覧）
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/messagePartnerList")
	public String messagePartnerList(@RequestParam(name="pid", defaultValue = "0") int cpid, ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";		
		User user = super.getSessionBean();
		int id = user.id;
		
		List<User> partnerList = new ArrayList<>();
		try {
			//pidがある場合はパートナー履歴に追加（チャットするから来た）
			if (cpid != 0)
				messageService.registPartner(id, cpid, jdbcTemplate);
		
			// パートナー一覧検索
			partnerList = messageService.getPartnerList(id, jdbcTemplate);
			modelMap.addAttribute("partnerList", partnerList);
			
			List<Chat> chatList = messageService.getMessageList(id, cpid, jdbcTemplate);
			modelMap.addAttribute("chatList", chatList);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}	
		// 選択したチャット画面の相手のpidを保持
		super.setSessionBeanInt("cpid", cpid);
		
		return "message";
		
	}
}
