package com.v2.controller.async;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.v2.bean.SessionBean;
import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.HtmlService;
import com.v2.service.LoginService;
import com.v2.service.MessageService;
import com.v2.service.ProfileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Scope("request")
public class RestInOutController extends AbstractController {
	
	@Autowired
	HtmlService htmlService;
	
	@Autowired
	MessageService messageService;
	
	@Autowired
	LoginService loginService;
	
	@Autowired
	ProfileService profileService;
	
	Logger logger = LoggerFactory.getLogger(RestInOutController.class);
	
	private final JdbcTemplate jdbcTemplate;

	public RestInOutController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/**
	 * ログイン処理
	 * @return
	 */
	@PostMapping("/restLogin")
	public String restLogin(@RequestParam("email") String email, 
			@RequestParam("password") String password,
			@CookieValue(value="uid", required=false) String uid,
			HttpServletResponse response) {
		
		User user = new User();
		// 各値チェック
		if (!loginService.doCheck(email, password)) {
			user.email = email;
			user.password = password;
			return "ログインに失敗しました";
		}
		
		try {			
				// ログイン処理
				if (uid == null)
					uid = super.getCookieHash();
				user = loginService.doLogin(email, password, uid, jdbcTemplate);

		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		
		// ログイン失敗時
		if (user.userId == null) {
			user = new User();
			user.email = email;
			user.password = password;
			return "ログインに失敗しました";
		}

		// ログイン成功時セッションにid保持
		SessionBean bean = new SessionBean();
		bean.id = user.id;
		super.setSessionBean(bean);
		
		// Cookieを埋め込む
		Cookie cookie = new Cookie("uid", uid);
		response.addCookie(cookie);
		
		return "ok";
	
	}
	/**
	 * ログアウト処理
	 * @return
	 */
	@PostMapping("/restLogout")
	public String restLogout(
			HttpServletRequest request, 
			HttpServletResponse response, 
			ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "ok";
		
		// ユーザid取得
		int id = super.getSessionBean().id;
		
		//セッションを破棄する
		super.removeSession();
		
		try {
			// ログアウト処理
			loginService.doLogout(id, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "ok";
		}
		
		// Cookie削除
	  Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
        if ("uid".equals(cookie.getName())) {
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
    }
		
		return "ok";
	
	}


}
