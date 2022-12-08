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
import com.v2.dao.TmessageUser;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class MessageService {
	
	private static final String DATE_PATTERN ="MM/dd HH:mm";
	
	@Autowired
	TmessageUser tMessageUser;
	
	/**
	 * パートナーリストを取得する
	 * @param id
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<User> getPartnerList(int id, JdbcTemplate jdbcTemplate) throws Exception {
		
		return tMessageUser.selectPartnerList(id, jdbcTemplate);
	
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
		
		return tMessageUser.selectMessageList(id, pid, jdbcTemplate);

	}
	
	public void doChat(int id, int pid, String message, JdbcTemplate jdbcTemplate) throws Exception {
		
		String messageRep = message.replaceAll("\r\n|\r|\n", "\n");
		
		// データベースに保存
		jdbcTemplate.update(
				"INSERT INTO T_CHAT_HIST VALUES (?, ?, ?, ?, current_timestamp, current_timestamp)",
						id, pid, id, messageRep);

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
		User user = tMessageUser.selectPartner(id, pid, jdbcTemplate);
		// いない場合だけ登録する
		if (user.id == 0)
			tMessageUser.insertPartner(id, pid, jdbcTemplate);
		
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
