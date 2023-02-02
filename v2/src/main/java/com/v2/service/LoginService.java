package com.v2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.v2.bean.User;
import com.v2.dao.MessageUserDao;
import com.v2.dao.UserDao;

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
  UserDao tUser;
  
  @Autowired
  MessageUserDao messageUserDao;
	
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
		// チャット新着メッセージがあるか確認する
		int cnt = messageUserDao.selectNewMessage(user.id, jdbcTemplate);
		user.chatTuchi = cnt; 
			
		return user;

	}

  /**
   * ログインする（CookieIDでログイン）
   * @param emal
   * @param password
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
	public User doLoginCookie(String uid, JdbcTemplate jdbcTemplate) throws Exception {
		
		// CookieIDでログイン
		User user = tUser.selectUserCookie(uid, jdbcTemplate);
		if (user.id > 0) {
  		// オンラインかつCookeiハッシュを更新する
  		tUser.updateOnlineOn(user.id, uid, jdbcTemplate);
  		// チャット新着メッセージがあるか確認する
  		int cnt = messageUserDao.selectNewMessage(user.id, jdbcTemplate);
  		user.chatTuchi = cnt; 
		}
		return user;

	}

  /**
   * ログインする（IDでログイン）
   * @param emal
   * @param password
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
	public User doLoginId(int id, JdbcTemplate jdbcTemplate) throws Exception {
		
		// IDでログイン
		User user = tUser.selectUser(id, jdbcTemplate);
		if (user.id > 0) {
  		// オンラインかつCookeiハッシュを更新する
  		tUser.updateOnlineOn(user.id, user.cookie, jdbcTemplate);
  		// チャット新着メッセージがあるか確認する
  		int cnt = messageUserDao.selectNewMessage(user.id, jdbcTemplate);
  		user.chatTuchi = cnt; 
		}
		return user;

	}

  /**
   * ログインステータスをONにする
   * @param emal
   * @param password
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
	public int onlineStatusOn(int id, JdbcTemplate jdbcTemplate) throws Exception {
		
		// オンラインフラグを立てる
		return tUser.updateOnlineStatus(id, 1, jdbcTemplate);

	}

  /**
   * ログインステータスをOFFにする
   * @param emal
   * @param password
   * @param jdbcTemplate
   * @return
   * @throws Exception
   */
	public int onlineStatusOff(int id, JdbcTemplate jdbcTemplate) throws Exception {
		
		// オンラインフラグを立てる
		return tUser.updateOnlineStatus(id, 0, jdbcTemplate);

	}

	/**
	 * ログアウトする
	 * @param id
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public int doLogout(int id, JdbcTemplate jdbcTemplate) throws Exception {
		return tUser.updateOnlineOff(id, jdbcTemplate);
	}
	
	/**
	 * COOKIEを削除する
	 * @param id
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public int delCookie(int id, JdbcTemplate jdbcTemplate) throws Exception {
		return tUser.updateDelCookie(id, jdbcTemplate);
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
