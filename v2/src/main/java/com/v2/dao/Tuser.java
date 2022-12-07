package com.v2.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.v2.bean.User;

@Component
public class Tuser {
	
	/**
	 * プロフィール一覧取得（古い順）
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<User> selectListOderRegist(JdbcTemplate jdbcTemplate) throws Exception {
		
		List<User> userList = new ArrayList<>();
		List<Map<String, Object>> dataList = jdbcTemplate
				.queryForList("SELECT * FROM ( "
						+ "SELECT * FROM T_USER WHERE TANKA > 0 ORDER BY REGIST_DATE "
						+ ") WHERE ROWNUM() <= 8"); // 新着順でデータベースから取り出す。

		for (Map<String, Object> data : dataList) {
  		userList.add(setUser(data));
		}
		return userList;
	}

	/**
	 * プロフィール一覧取得（新着順）
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<User> selectListOderRegistDesc(JdbcTemplate jdbcTemplate) throws Exception {
		
		List<User> userList = new ArrayList<>();
		List<Map<String, Object>> dataList = jdbcTemplate
				.queryForList("SELECT * FROM ( "
						+ "SELECT * FROM T_USER WHERE TANKA > 0 ORDER BY REGIST_DATE DESC "
						+ ") WHERE ROWNUM() <= 8"); // 新着順でデータベースから取り出す。

		for (Map<String, Object> data : dataList) {
  		userList.add(setUser(data));
		}
		return userList;
	}

	/**
	 * プロフィール一覧取得（オーダー数順）
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<User> selectListOderSum(JdbcTemplate jdbcTemplate) throws Exception {
		
  	List<User> userList = new ArrayList<>();
  	List<Map<String, Object>> dataList = jdbcTemplate
				.queryForList("SELECT * FROM ( "
						+ "SELECT * FROM T_USER WHERE TANKA > 0 ORDER BY ORDER_SUM DESC "
						+ ") WHERE ROWNUM() <= 8"); // オーダー数順でデータベースから取り出す。
  
  	for (Map<String, Object> data : dataList) {
  		userList.add(setUser(data));
  	}
  	return userList;
	}
	
	/**
	 * ユーザ情報取得（ID指定）
	 * @param id
	 * @return
	 */
	public User selectUser(int id, JdbcTemplate jdbcTemplate) throws Exception {

		List<Map<String, Object>> dataList = jdbcTemplate.queryForList("SELECT * FROM T_USER WHERE ID = ?", id); // IDが一致するものを取得
		if (dataList.size() == 1) 
			return setUser(dataList.get(0));
		return new User();
	
	}

	/**
	 * ユーザ情報取得（Cookie指定）
	 * @param id
	 * @return
	 */
	public User selectUserCookie(String uid, JdbcTemplate jdbcTemplate) throws Exception {

		List<Map<String, Object>> dataList = jdbcTemplate.queryForList("SELECT * FROM T_USER WHERE COOKIE = ?", uid);
		if (dataList.size() == 1) 
			return setUser(dataList.get(0));
		return new User();
	
	}

	/**
	 * ユーザ情報取得（Emain、パスワード指定）
	 * @param emal
	 * @param password
	 * @param jdbcTemplate
	 * @return
	 */
	public User selectUserEmail(String emal, String password, JdbcTemplate jdbcTemplate) throws Exception {
		
		List<Map<String, Object>> dataList = jdbcTemplate.queryForList(
				"SELECT * FROM T_USER WHERE E_MAIL = ? and PASSWORD = ?",
				emal, password); // IDが一致するものを取得
		
		if (dataList.size() == 1) 
			return setUser(dataList.get(0));
		return new User();
		
	}

  /**
   * オンラインステータスをONにする（Cookieも登録する）
   * @param emal
   * @param password
   * @param jdbcTemplate
   * @return
   */
  public int updateOnlineOn(int id, String uid, JdbcTemplate jdbcTemplate) throws Exception {
  	
  	int cnt = jdbcTemplate.update(
  			"UPDATE T_USER SET " +
  			"ONLINE_STATUS=? ,COOKIE=?, UPDATE_DATE=current_timestamp " +
  			"WHERE  ID=?",
  			1, uid, id);		
  	return cnt;
  
  }

  /**
   * オンラインステータスをOFFにする
   * @param emal
   * @param password
   * @param jdbcTemplate
   * @return
   */
  public int updateOnlineOff(int id, JdbcTemplate jdbcTemplate) throws Exception {
  	
  	int cnt = jdbcTemplate.update(
  			"UPDATE T_USER SET " +
  			"ONLINE_STATUS=? ,UPDATE_DATE=current_timestamp " +
  			"WHERE  ID=?",
  			0, id);		
  	return cnt;
  
  }

  /**
   * プロフィールを更新する
   * @param emal
   * @param password
   * @param jdbcTemplate
   * @return
   */
  public int updateProfileDetail(User user, JdbcTemplate jdbcTemplate) throws Exception {
  	
		String messageRep = null;
		// 改行コードを置換
		if (StringUtils.hasLength(user.message))
			messageRep = user.message.replaceAll("\r\n|\r|\n", "\n");

		int cnt = jdbcTemplate.update(
				"UPDATE T_USER SET " +
						"USER_NAME=?, IMAGE_PATH=?, MESSAGE=?, YOUTUBE_URL=?, TWITTER_URL=?, TIKTOK_URL=?, INSTA_URL=?, UPDATE_DATE=current_timestamp " +
						"WHERE  ID=?",
						user.userName, user.imageFile, messageRep, user.youtube, user.twitter, user.tiktok, user.insta,  user.id);
  	return cnt;
  
  }

  /**
   * 出品情報を更新する
   * @param emal
   * @param password
   * @param jdbcTemplate
   * @return
   */
  public int updateProfileShop(User user, JdbcTemplate jdbcTemplate) throws Exception {
  	
		int cnt = jdbcTemplate.update(
				"UPDATE T_USER SET " +
						"IMAGE_PATH=?, TITLE=?, TANKA=?, UPDATE_DATE=current_timestamp " +
						"WHERE  ID=?",
						user.imageFile, user.title, user.tanka, user.id);
  	return cnt;
  
  }

  /**
   * 出品情報を更新する（画像なし）
   * @param emal
   * @param password
   * @param jdbcTemplate
   * @return
   */
  public int updateProfileShopNoImage(User user, JdbcTemplate jdbcTemplate) throws Exception {
  	
		int cnt = jdbcTemplate.update(
				"UPDATE T_USER SET " +
						"TITLE=?, TANKA=?, UPDATE_DATE=current_timestamp " +
						"WHERE  ID=?",
						user.title, user.tanka, user.id);
  	return cnt;
  
  }

  /**
   * ユーザIDのシーケンス取得
   * @param jdbcTemplate
   * @return
   */
  public int getSequenceUserId(JdbcTemplate jdbcTemplate) throws Exception {
  	
  	// ユーザIDのシーケンス取得
  	return jdbcTemplate.queryForObject("SELECT NEXT VALUE FOR USER_ID_SEQ", Integer.class);
  
  }
  
  /**
   * プロフィール新規登録
   * @param user
   * @param jdbcTemplate
   */
  public void insertProfile(User user, JdbcTemplate jdbcTemplate) throws Exception {
  	
		// プロフィール新規登録
		jdbcTemplate.update(
		    "INSERT INTO T_USER " +
		        "( ID, USER_ID, USER_NAME , PASSWORD, E_MAIL, SEX, IMAGE_PATH, REGIST_DATE, UPDATE_DATE ) " +
		        "VALUES (?, ?, ?, ?, ?, ?, ?, current_timestamp, current_timestamp)",
		    user.id, user.userId, user.userName, user.password, user.email, user.sex, "default.png");

  }

  /**
	 * ユーザ情報をセットする
	 * @param data
	 * @return
	 */
	private User setUser(Map<String, Object> data) {
		
		User user = new User();
		user.id = (int) data.get("ID");
		user.userId = (String)data.get("USER_ID");
		user.userName = (String) data.get("USER_NAME");
		user.password = (String) data.get("PASSWORD");
		user.email = (String) data.get("E_MAIL");
		user.sex = (String) data.get("SEX");
		if (data.get("IMAGE_PATH") != null) 
			user.imageFile = (String) data.get("IMAGE_PATH");
		if (data.get("TITLE") != null) 
			user.title = (String) data.get("TITLE");
		if (data.get("MESSAGE") != null) 
			user.message = (String) data.get("MESSAGE");
		user.tanka = (int) data.get("TANKA");
		user.evaluation = (BigDecimal) data.get("EVALUATION");
		user.orderSum = (int) data.get("ORDER_SUM");
		if (data.get("INSTA_URL") != null) 
			user.insta = (String) data.get("INSTA_URL");
		if (data.get("TWITTER_URL") != null) 
			user.twitter = (String) data.get("TWITTER_URL");
		if (data.get("YOUTUBE_URL") != null) 
			user.youtube = (String) data.get("YOUTUBE_URL");
		if (data.get("TIKTOK_URL") != null) 
			user.tiktok = (String) data.get("TIKTOK_URL");
		if (data.get("COOKIE") != null) 
			user.cookie = (String) data.get("COOKIE");
		user.registDate = (Date) data.get("REGIST_DATE");
		user.updateDate = (Date) data.get("UPDATE_DATE");
		user.onlineStatus = (int) data.get("ONLINE_STATUS");
		user.registStatus = (int) data.get("REGIST_STATUS");
		return user;
		
	}

}
