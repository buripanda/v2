package com.v2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.v2.bean.User;
import com.v2.dao.Tuser;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class LoginService {
	
  private final MailSender mailSender;

  public LoginService(MailSender mailSender) { 
      this.mailSender = mailSender;
  }
  
  @Autowired
  Tuser tUser;
	
  /**
   * ログインする
   * @param emal
   * @param password
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
	public User doLogin(String emal, String password, String uid, JdbcTemplate jdbcTemplate) throws Exception {
		
		// CookieとDBのハッシュ値が同じか確認する
		User user = tUser.selectUserCookie(uid, jdbcTemplate);
		// Cookieが一致しない場合はログイン処理を行う
		if (user.id == 0) {
			// Email、パスワードで検索
			user = tUser.selectUserEmail(emal, password, jdbcTemplate);
			// Email、パスワードも一致しない場合はログインできない
			if (user.id == 0)
				return user;
			
		}
		// オンラインかつCookeiハッシュを更新する
		tUser.updateOnlineOn(user.id, uid, jdbcTemplate);
			
		return user;

	}
	
	public int doLogout(int id, JdbcTemplate jdbcTemplate) throws Exception {
		return tUser.updateOnlineOff(id, jdbcTemplate);
	}
	
	/**
	 * 各値をチェック
	 * @param email
	 * @param password
	 * @param agree
	 * @return
	 */
	public boolean doCheck(String email, String  password) {
		
		// emailチェック
		if (!StringUtils.hasLength(email))
			return false;
		// passwordチェック
		if (!StringUtils.hasLength(password))
			return false;
		return true;

	}
	
	public void doMail() throws Exception {
		
    SimpleMailMessage msg = new SimpleMailMessage();
    msg.setFrom("test@example.com"); // 送信元メールアドレス
    msg.setTo("buripanda@gmail.com"); // 送信先メールアドレス
    msg.setSubject("テストタイトル"); // タイトル               
    msg.setText("テスト本文\r\n改行します。"); //本文
    mailSender.send(msg);
	
	}
}
