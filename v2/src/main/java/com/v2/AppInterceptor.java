package com.v2;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.v2.bean.User;
import com.v2.service.LoginService;

/**
 * アプリケーションイベントハンドラー
 * @author burip
 *
 */
public class AppInterceptor implements HandlerInterceptor {
	
	@Autowired
	LoginService loginService;
	
	private final JdbcTemplate jdbcTemplate;

	public AppInterceptor(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
	    HttpServletResponse response,
	    Object handler) throws Exception {
		
		String url = request.getRequestURI();
		if (url.contains("getImg") || url.contains("font/") || url.contains("image/") || url.contains("js/") || url.contains("css/")) {
			return true;
		}
		// セッションチェック
		Object obj = request.getSession().getAttribute("v2bean");
		// セッションBeanがない場合はCookieチェック
		if (obj == null) {
			if (request.getCookies() != null) {
  			for (Cookie cookie : request.getCookies()) {
  					//Cookieにuidがあればユーザ情報取得
  					if ("uid".equals(cookie.getName())) {
  						System.out.println("Cookieが存在：" + cookie.getValue());
  						User user = loginService.doLoginCookie(cookie.getValue(), jdbcTemplate);
  						// CookieがDBにあればセッション作成
  						if (user.id > 0) {
  							request.getSession().setAttribute("v2bean", user);
  							
  						}
  					}
  			}
			}
  		// ユーザ情報取得しなおし
		} else {
			System.out.println("ユーザ情報取得しなおし" + ((User)obj).id);
			User user = loginService.doLoginId(((User)obj).id, jdbcTemplate);
			request.getSession().setAttribute("v2bean", user);
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request,
	    HttpServletResponse response, Object handler,
	    ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
	    HttpServletResponse response, Object handler, Exception ex)
	    throws Exception {
	}

}
