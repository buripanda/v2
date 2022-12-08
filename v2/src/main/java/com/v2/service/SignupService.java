package com.v2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.v2.bean.User;
import com.v2.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SignupService {
	
	@Autowired
	UserDao tUser;
	
	@Autowired
	ImageService imageService;
	
	/**
	 * プロフィール新規登録
	 * @param user
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public User doSignup(User user, JdbcTemplate jdbcTemplate) throws Exception {

		//シーケンス取得
		int seq = tUser.getSequenceUserId(jdbcTemplate);
		
		//デフォルト画像登録
		imageService.saveImageFileDefault(seq);

		user.id = seq;
		user.userId = String.valueOf(seq);

		// プロフィール新規登録
		tUser.insertProfile(user, jdbcTemplate);

		return user;

	}

	/**
	 * 各値をチェック
	 * @param email
	 * @param password
	 * @param agree
	 * @return
	 */
	public boolean doCheck(User user, String agree) {
		
		// userNameチェック
		if (!StringUtils.hasLength(user.userName))
			return false;
		// passwordチェック
		if (!StringUtils.hasLength(user.password))
			return false;
		// emailチェック
		if (!StringUtils.hasLength(user.email))
			return false;
		// agreeチェック
		if (!StringUtils.hasLength(agree)) 
			return false;
		if (StringUtils.hasLength(agree)) {
			if (agree.length() != 2) 
				return false;
		}
		return true;

	}
	
}
