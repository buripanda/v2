package com.v2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.v2.bean.PropartyConfig;
import com.v2.bean.User;
import com.v2.dao.Tuser;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ProfileService {
	
	/**
	 * イメージ保存先パス
	 */
	//private static String IMAGE_PATH = "C:\\Tomcat10\\webapps\\ROOT\\images\\";
	private static String IMAGE_PATH = "C:\\data\\images\\";
	
	/**
	 * イメージ保存先パス（サムネイル）
	 */
	//private static String IMAGE_THUMBNAIL_PATH = "C:\\Tomcat10\\webapps\\ROOT\\images\\thumbnail\\";
	private static String IMAGE_THUMBNAIL_PATH = "C:\\data\\images\\thumbnail\\";
	
	@Autowired
	PropartyConfig propartyConfig;
	
	@Autowired
	ImageService imageService;
	
	@Autowired
	Tuser tUser;
	
	/**
	 * プロフィール情報取得
	 * @param id
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public User getProfile(int id, JdbcTemplate jdbcTemplate) throws Exception {
		
		// プロフィール情報取得
		return tUser.selectUser(id, jdbcTemplate);

	}
	
	/**
	 * プロフィール変更時のパラメータチェック
	 * @param user
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public boolean isProfileEdit(User user) throws Exception {
		
			// userNameチェック
			if (!StringUtils.hasLength(user.userName))
				return false;
			return true;
		
	}
	
	/**
	 * プロフィールを更新する
	 * @param user
	 * @param imageFile
	 * @param jdbcTemplate
	 * @throws Exception
	 */
	public void doProfileModify(User user, MultipartFile imageFile, JdbcTemplate jdbcTemplate) throws Exception {
		
		// 画像が変更された場合は画像保存
		if (!imageFile.isEmpty()) {
			imageService.saveImageFile(user, imageFile);
			tUser.updateProfileDetail(user, jdbcTemplate);				
		// 画像がない場合は画像に関して何もしない
		} else {
			tUser.updateProfileDetailNoImage(user, jdbcTemplate);
		}
		
	}
}
