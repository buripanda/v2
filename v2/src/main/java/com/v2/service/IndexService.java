package com.v2.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.v2.bean.User;
import com.v2.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class IndexService {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	UserDao tUser;
	
	
	
	/**
	 * プロフィール一覧取得（古い順）
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<User> getProfileListOderRegist(JdbcTemplate jdbcTemplate) throws Exception {
		List<User> userList = tUser.selectListOderRegist(jdbcTemplate);	
		return userList;

	}

	/**
	 * プロフィール一覧取得（新着順）
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<User> getProfileListOderRegistDesc(JdbcTemplate jdbcTemplate) throws Exception {
		List<User> userList = tUser.selectListOderRegistDesc(jdbcTemplate);	
		return userList;

	}

	/**
	 * プロフィール一覧取得（オーダー数順）
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<User> getProfileListOderSum(JdbcTemplate jdbcTemplate) throws Exception {
		List<User> userList = tUser.selectListOderSum(jdbcTemplate);
		return userList;
	}
	
	/**
	 * プロフィール一覧取得（キーワード検索）
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public List<User> getProfileListKeyword(String keyword, JdbcTemplate jdbcTemplate) throws Exception {
		List<User> userList = tUser.selectListKeyword(keyword, jdbcTemplate);	
		return userList;

	}



}