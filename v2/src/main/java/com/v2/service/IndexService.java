package com.v2.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.v2.bean.User;
import com.v2.dao.Tuser;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class IndexService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	Tuser tUser;
	
	/**
	 * プロフィール一覧取得（古い順）
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<User> getProfileListOderRegist(JdbcTemplate jdbcTemplate) throws Exception {
		List<User> userList = tUser.selectListOderRegist(jdbcTemplate);	
		List<User> userList2 = new ArrayList<>();
	  for (User user : userList) {
	  	if (user.title != null && user.title.length() > 12) {
	  		user.title = user.title.substring(0,12);
	  	}
	  	userList2.add(user);
	  }
		return userList2;

	}

	/**
	 * プロフィール一覧取得（新着順）
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<User> getProfileListOderRegistDesc(JdbcTemplate jdbcTemplate) throws Exception {
		List<User> userList = tUser.selectListOderRegistDesc(jdbcTemplate);	
		List<User> userList2 = new ArrayList<>();
	  for (User user : userList) {
	  	if (user.title != null && user.title.length() > 12) {
	  		user.title = user.title.substring(0,12);
	  	}
	  	userList2.add(user);
	  }
		return userList2;

	}

	/**
	 * プロフィール一覧取得（オーダー数順）
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<User> getProfileListOderSum(JdbcTemplate jdbcTemplate) throws Exception {
		List<User> userList = tUser.selectListOderSum(jdbcTemplate);
		List<User> userList2 = new ArrayList<>();
	  for (User user : userList) {
	  	if (user.title != null && user.title.length() > 12) {
	  		user.title = user.title.substring(0,12);
	  	}
	  	userList2.add(user);
	  }
		return userList2;
	}
	


}