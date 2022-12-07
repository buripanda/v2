package com.v2.controller.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.v2.bean.SessionBean;
import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.SignupService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
public class SignupController extends AbstractController {
	
	@Autowired
	SignupService signupService;

	private final JdbcTemplate jdbcTemplate;

	public SignupController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 新規登録画面（表示）
	 * @return
	 */
	@GetMapping("/signup")
	public String signupGet(ModelMap modelMap) {
		
		//性別初期値（女性）
		User user = new User();
		user.sexChecked = 1;
		modelMap.addAttribute("user", user);
		return "signup";
	}
	
	/**
	 * 新規登録画面（登録処理）
	 * @param userName
	 * @param password
	 * @param eMail
	 * @param sex
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/signup")
	public String signupPost(@RequestParam("userName") String userName,
			@RequestParam("password") String password,
			@RequestParam("email") String email,
			@RequestParam("sex") String sex,
			@RequestParam(name = "agree", required = false) String agree,
			ModelMap modelMap) {
		
		User user = new User();
		user.userName = userName;
		user.email = email;
		user.password = password;
		user.sex = sex;
		if ("2".equals(sex)) {
			user.sexChecked = 2;
		} else {
			user.sexChecked = 1;
		}

		// 各値チェック
		if (!signupService.doCheck(user, agree)) {
			modelMap.addAttribute("user", user);
			return "signup";
		}
		
		//ユーザ登録
		try {
			user = signupService.doSignup(user, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		
		//セッションにid保持
		SessionBean bean = new SessionBean();
		bean.id = user.id;
		super.setSessionBean(bean);		
		modelMap.addAttribute("user", user);

		return "forward:index";
	}
}
