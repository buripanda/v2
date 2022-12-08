package com.v2.dao;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.v2.bean.Chat;
import com.v2.bean.User;

@Component
public class MessageUserDao {

	/**
	 * パートナーリストを取得する
	 * @param id
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<User> selectPartnerList(int id, JdbcTemplate jdbcTemplate) throws Exception {
		
		List<Map<String, Object>> dataList = jdbcTemplate.queryForList(
				"SELECT"
				+ "  T1.PARTNER_ID, "
				+ "  T2.USER_NAME, "
				+ "  T2.IMAGE_PATH, "
				+ "  T1.LAST_MESSAGE, "
				+ "  T1.UPDATE_DATE "
				+ "FROM"
				+ "  T_MESSAGE_USER T1"
				+ "  INNER JOIN T_USER T2"
				+ "  ON T1.PARTNER_ID = T2.ID "
				+ " WHERE"
				+ "  T1.ID = ? "
				+ "  AND T1.HIDDEN_STATUS = 0 "
				+ "ORDER BY"
				+ "  T1.UPDATE_DATE DESC", 
				id); 
		
		List<User> partnerList = new ArrayList<>();
		for (Map<String, Object> data : dataList) {
			User user = new User();
			user.id = (int)data.get("PARTNER_ID");
			user.userName = (String)data.get("USER_NAME");
			user.imageFile = (String) data.get("IMAGE_PATH");
			user.message = (String) data.get("LAST_MESSAGE");
			user.updateDate = (Date)data.get("UPDATE_DATE");
			partnerList.add(user);
		}
		return partnerList;

	}

	/**
	 * パートナーを取得する
	 * @param id
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public User selectPartner(int id, int pid, JdbcTemplate jdbcTemplate) throws Exception {
		
		List<Map<String, Object>> dataList  = jdbcTemplate.queryForList(
				"SELECT"
				+ "  PARTNER_ID "
				+ "FROM"
				+ "  T_MESSAGE_USER "
				+ " WHERE"
				+ "  ID = ? "
				+ "  AND PARTNER_ID = ? ",
				id, pid); 
		
		User user = new User();
		for (Map<String, Object> data : dataList) {
			user.id = (int)data.get("PARTNER_ID");
			user.userName = (String)data.get("USER_NAME");
			user.imageFile = (String) data.get("IMAGE_PATH");
			user.message = (String) data.get("LAST_MESSAGE");
			user.updateDate = (Date)data.get("UPDATE_DATE");
		}
		return user;

	}

	/**
	 * パートナーを登録する
	 * @param id
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public void insertPartner(int id, int pid, JdbcTemplate jdbcTemplate) throws Exception {
		
		// メッセージユーザーに登録
		jdbcTemplate.update(
				"INSERT INTO T_MESSAGE_USER VALUES (?, ?, null, 0, current_timestamp, current_timestamp)",
				id, pid); 
	}

	/**
	 * 自分の最新メッセージを更新する
	 * @param id
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public void updateMessage(int id, int pid, String message, JdbcTemplate jdbcTemplate) throws Exception {
		
		jdbcTemplate.update(
				"UPDATE T_MESSAGE_USER SET LAST_MESSAGE = ?, UPDATE_DATE=current_timestamp "
				+ " WHERE ID=? AND PARTNER_ID=?",
				message, pid, id); 
	}

	/**
	 * チャットリストを取得する
	 * @param id
	 * @param partnaerId
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<Chat> selectMessageList(int id, int pid, JdbcTemplate jdbcTemplate) throws Exception {
		
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
				id, pid,id, pid); 
		
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
  		str = new SimpleDateFormat("MM/dd HH:mm").format(date);
  	}
  	return str;
  }

}
