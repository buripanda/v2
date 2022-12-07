package com.v2.controller.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.IndexService;
import com.v2.service.LoginService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
public class LoginController extends AbstractController {

	
	@Autowired
	LoginService loginService;
	
	@Autowired
	IndexService indexService;

	private final JdbcTemplate jdbcTemplate;

	public LoginController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	/**
	 * ログイン画面（表示）
	 * @return
	 */
	@GetMapping("/login")
	public String loginGet(ModelMap modelMap) {
		User user = new User();
		modelMap.addAttribute("user", user);
		return "login";
	}
	
	/**
	 * ログアウト処理
	 * @return
	 */
	@GetMapping("/logout")
	public String logoutGet(ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "forward:index";
		
		super.getSessionBean();
		int id = super.getSessionBean().id;
		
		//セッションを破棄する
		removeSession();
		
		try {
			// ログアウト処理
			loginService.doLogout(id, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		
		return "redirect:/";
	
	}

}
