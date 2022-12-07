package com.v2.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.v2.bean.User;

@Component
public class TmessageUser {

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
}
