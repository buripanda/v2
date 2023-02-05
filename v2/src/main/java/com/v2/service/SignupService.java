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
		
		// 自己紹介文新規登録
		tUser.insertUserMessage(user, jdbcTemplate);

		return user;

	}
	
	/**
	 * ユーザ重複チェック
	 * @param user
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public boolean isExistUser(User user, JdbcTemplate jdbcTemplate) throws Exception {

		// ユーザが存在しているか確認する
		int cnt = tUser.selectUserCntEmail(user.email, jdbcTemplate);
		if (cnt == 0)
			return true;
		return false;

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
		if (!StringUtils.hasLength(user.userName)) {
		  user.errorMessage = "ユーザ名を入力してください";
			return false;
		}
    if (user.userName.length() > 20) {
      user.errorMessage = "ユーザ名は20文字以内で入力してください";
      return false;
    }
		
		// passwordチェック
		if (!StringUtils.hasLength(user.password)) {
      user.errorMessage = "パスワードを入力してください";
			return false;
		}
    if (user.password.length() < 8 || user.password.length() >16) {
      user.errorMessage = "パスワードは8文字以上16文字以下で入力してください";
      return false;
    }
		// emailチェック
		if (!StringUtils.hasLength(user.email)) {
      user.errorMessage = "メールアドレスを入力してください";
			return false;
		}
    if (user.email.length() < 100) {
      user.errorMessage = "メールアドレスは100文字以下で入力してください";
      return false;
    }
		// agreeチェック
		if (!StringUtils.hasLength(agree)) {
      user.errorMessage = "利用規約に同意してください";		  
			return false;
		}
		return true;

	}
	
}
