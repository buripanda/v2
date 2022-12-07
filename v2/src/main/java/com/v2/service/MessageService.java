package com.v2.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

	
	public List<Chat> doView(int id, int partnaerId, JdbcTemplate jdbcTemplate) throws Exception {
		
		List<Map<String, Object>> dataList = jdbcTemplate.queryForList(
				"SELECT"
				+ "  T1.SEND_ID, "
				+ "  T1.RECEIVE_ID, "
				+ "  T2.USER_NAME AS SEND_NAME, "
				+ "  T3.USER_NAME AS RECEIVE_NAME, "
				+ "  T2.IMAGE_PATH AS SEND_IMAGE_PATH, "
				+ "  T3.IMAGE_PATH AS RECEIVE_IMAGE_PATH, "
				+ "  T1.MESSAGE, "
				+ "  T1.REGIST_DATE "
				+ "FROM"
				+ "  T_MESSAGE_HIST T1"
				+ "  LEFT OUTER JOIN T_USER T2"
				+ "  ON T1.SEND_ID = T2.ID "
				+ "  LEFT OUTER JOIN T_USER T3"
				+ "  ON T1.RECEIVE_ID = T3.ID "
				+ " WHERE"
				+ "  T1.SEND_ID IN (?, ?) "
				+ "  AND T1.SEND_ID IN (?, ?) "
				+ "ORDER BY"
				+ "  T1.REGIST_DATE", 
				id, partnaerId,id, partnaerId); 
		
		List<Chat> chatList = new ArrayList<>();
		for (Map<String, Object> data : dataList) {
			Chat chat = new Chat();
			chat.sendId = (int) data.get("SEND_ID");
			chat.receiveId = (int) data.get("RECEIVE_ID");
			chat.loginId = id;
			chat.sendName = (String) data.get("SEND_NAME");
			chat.receiveName = (String) data.get("RECEIVE_NAME");
			chat.sendImagePath = (String) data.get("SEND_IMAGE_PATH");
			chat.receiveImagePath = (String) data.get("RECEIVE_IMAGE_PATH");
			chat.message = (String) data.get("MESSAGE");
			chat.registDate = parseDateToString((Date)data.get("REGIST_DATE"));
			chatList.add(chat);
		}
		return chatList;
	}
	
	public void doChat(int id, int pId, String message, JdbcTemplate jdbcTemplate) throws Exception {
		
		String messageRep = message.replaceAll("\r\n|\r|\n", "\n");
		
		// データベースに保存
		jdbcTemplate.update(
				"INSERT INTO T_CHAT_HIST VALUES (?, ?, ?, ?, current_timestamp, current_timestamp)",
						id, pId, id, messageRep);


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
