package com.v2.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.v2.bean.Chat;
import com.v2.bean.User;
import com.v2.dao.MessageHistDao;
import com.v2.dao.MessageUserDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class MessageService {
	
	private static final String DATE_PATTERN ="MM/dd HH:mm";
	
	@Autowired
	MessageUserDao messageUserDao;
	
	@Autowired
	MessageHistDao messageHistDao;
	
	/**
	 * パートナーリストを取得する
	 * @param id
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<User> getPartnerList(int id, JdbcTemplate jdbcTemplate) throws Exception {
		
		return messageUserDao.selectPartnerList(id, jdbcTemplate);
	
	}

	/**
	 * メッセージリストを取得する
	 * @param id
	 * @param pid
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<Chat> getMessageList(int id, int pid, JdbcTemplate jdbcTemplate) throws Exception {
		
		// 新着メッセージを既読にする
		messageUserDao.updateNewMessage(id, pid, 0, jdbcTemplate);
		// チャットリスト取得
		return messageUserDao.selectMessageList(id, pid, jdbcTemplate);

	}
	
	/**
	 * メッセージを登録する
	 * @param id
	 * @param pid
	 * @param message
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public void registMessage(int id, int pid, String message, JdbcTemplate jdbcTemplate) throws Exception {
		
		// メッセージを登録
		messageHistDao.insertMessage(id, pid, message, jdbcTemplate);

		// 最新メッセージを登録
		messageUserDao.updateMessage(id, pid, message, jdbcTemplate);
		
	}

	/**
	 * メッセージを登録して、リストを返す
	 * @param id
	 * @param pid
	 * @param message
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<Chat> registMessageRetList(int id, int pid, String message, JdbcTemplate jdbcTemplate) throws Exception {
		
		this.registMessage(id, pid, message, jdbcTemplate);
		
		// メッセージリストを取得する
		List<Chat> chatList = messageUserDao.selectMessageList(id, pid, jdbcTemplate);
		return chatList;

	}
	
	/**
	 * メッセージユーザを登録する
	 * @param id
	 * @param pid
	 * @param jdbcTemplate
	 * @throws Exception
	 */
	public void registPartner(int id, int pid, JdbcTemplate jdbcTemplate) throws Exception {
		
		// メッセージユーザを検索する
		User user = messageUserDao.selectPartner(id, pid, jdbcTemplate);
		// いない場合だけ登録する
		if (user.id == 0)
			messageUserDao.insertPartner(id, pid, jdbcTemplate);
		
	}

	
	/**
     * <p>[概 要] Date型⇒String型への変換処理</p>
     * <p>[詳 細] </p>
     * <p>[備 考] </p>
     * @param  date 変換前日付オブジェクト
     * @return String型オブジェクト（変換に失敗した場合はnullを返します。）
     */
	public static String parseDateToString(Date date){
		String  str;
		if(date == null) {
			str = null;
		} else {
			str = new SimpleDateFormat(DATE_PATTERN).format(date);
		}
		return str;
	}
}
