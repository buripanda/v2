package com.v2.controller.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.v2.bean.Recruit;
import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.ProfileService;
import com.v2.service.RecruitService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
public class RecruitController extends AbstractController {
	
	@Autowired
	RecruitService recruitService;	
	
	@Autowired
	ProfileService profileService;
	
	private final JdbcTemplate jdbcTemplate;

	public RecruitController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 募集画面表示
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@GetMapping("/recruitView")
	public String recruitViewG(ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";		
		return recruitView(modelMap);
	
	}
	
	/**
	 * 募集画面表示
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/recruitView")
	public String recruitView(ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";		
		User user = super.getSessionBean();
		int id = user.id;
		
		Recruit recruit = new Recruit();
		try {
			// 募集メッセージ取得
			recruit = recruitService.viewRecruit(id, jdbcTemplate);
			// プロフィール取得
			user = profileService.getProfilePlusMessage(id, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		modelMap.addAttribute("recruit", recruit);
		modelMap.addAttribute("user", user);
		return "recruit";
	}
}
