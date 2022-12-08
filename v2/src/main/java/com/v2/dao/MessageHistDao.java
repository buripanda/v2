package com.v2.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageHistDao {

	/**
	 * メッセージを登録する
	 * @param id
	 * @param pid
	 * @param message
	 * @param jdbcTemplate
	 * @throws Exception
	 */
	public void insertMessage(int id, int pid, String message, JdbcTemplate jdbcTemplate) throws Exception {
		
		String messageRep = message.replaceAll("\r\n|\r|\n", "\n");
		
		// データベースに保存
		jdbcTemplate.update(
				"INSERT INTO T_MESSAGE_HIST VALUES (?, ?, ?, current_timestamp, current_timestamp)",
						id, pid, messageRep);

	}

}
