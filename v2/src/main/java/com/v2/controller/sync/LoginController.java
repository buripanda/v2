package com.v2.controller.sync;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

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
	@PostMapping("/logout")
	public String logout(
			HttpServletRequest request, 
			HttpServletResponse response, 
			ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";
		
		super.getSessionBean();
		int id = super.getSessionBean().id;
		
		//セッションを破棄する
		super.removeSession();
		
		try {
			// ログアウト処理
			loginService.doLogout(id, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		
		// Cookie削除
	  Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
        if ("uid".equals(cookie.getName())) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }

		
		return "redirect:/";
	
	}

}
