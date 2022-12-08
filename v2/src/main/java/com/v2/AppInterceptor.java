package com.v2;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.v2.bean.SessionBean;
import com.v2.bean.User;
import com.v2.dao.Tuser;

/**
 * アプリケーションイベントハンドラー
 * @author burip
 *
 */
public class AppInterceptor implements HandlerInterceptor {
	
	@Autowired
	Tuser tUser;
	
	private final JdbcTemplate jdbcTemplate;

	public AppInterceptor(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public boolean preHandle(HttpServletRequest request,
	    HttpServletResponse response,
	    Object handler) throws Exception {
		
		// セッションチェック
		Object obj = request.getSession().getAttribute("v2bean");
		// セッションBeanがない場合はCookieチェック
		if (obj == null) {
			if (request.getCookies() != null) {
  			for (Cookie cookie : request.getCookies()) {
  					//CookieにuidがあればDB検索
  					if ("uid".equals(cookie.getName())) {
  						System.out.println("リクエスト中のCookie：" + cookie.getValue());
  						User user = tUser.selectUserCookie(cookie.getValue(), jdbcTemplate);
  						// CookieがDBにあればセッション作成
  						if (user.id > 0) {
  							SessionBean bean = new SessionBean();
  							bean.id = user.id;
  							request.getSession().setAttribute("v2bean", bean);
  							
  						}
  					}
  			}
			}
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
