package com.v2;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
		System.out.println("セッションが張られました");
		System.out.println("セッションID：" + session.getId());		
		
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent se) {
  	
    // セッション破棄時の処理
		HttpSession session = se.getSession();
		System.out.println("セッションが破棄されました");
		System.out.println("セッションID：" + session.getId());		
		
  }
}