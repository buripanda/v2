package com.v2;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.v2.bean.SessionBean;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class HttpSessionDestroyedEventHandler implements HttpSessionListener {
	
	private final JdbcTemplate jdbcTemplate;
	
	public HttpSessionDestroyedEventHandler(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	
	@Override
  public void sessionCreated(HttpSessionEvent se) {
		
		
		HttpSession session = se.getSession();
		SessionBean bean = new SessionBean();
		
		Object obj = session.getAttribute("v2bean");
		if (obj != null)
			bean = (SessionBean)obj;
		System.out.println("セッションが張られました");
		System.out.println("有効期限：" + session.getMaxInactiveInterval());
		long from =  session.getLastAccessedTime();
		long now = System.currentTimeMillis();
		System.out.println("経過時間：" + (now - from));		
		System.out.println("セッションID：" + session.getId());		
		System.out.println("ユーザID=" + bean.id);
		
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent se) {
  	
  	
    // セッション破棄時の処理
		HttpSession session = se.getSession();
		SessionBean bean = new SessionBean();
		
		Object obj = session.getAttribute("v2bean");
		if (obj != null)
			bean = (SessionBean)obj;
		System.out.println("セッションが破棄されました");
		System.out.println("有効期限：" + session.getMaxInactiveInterval());
		long from =  session.getLastAccessedTime();
		long now = System.currentTimeMillis();
		System.out.println("経過時間：" + (now - from));		
		System.out.println("セッションID：" + session.getId());		
		System.out.println("ユーザID=" + bean.id);
		jdbcTemplate.update(
				"UPDATE T_USER SET " +
						"ONLINE_STATUS=? ,UPDATE_DATE=current_timestamp " +
						"WHERE  ID=?",
						0, bean.id);
		
  }
}