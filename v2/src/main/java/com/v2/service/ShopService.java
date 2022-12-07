package com.v2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.v2.bean.User;
import com.v2.dao.Tuser;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ShopService {
	
	@Autowired
	ImageService imageService;
	
	@Autowired
	Tuser tUser;
	
	/**
	 * パラメータチェック
	 * @param user
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public boolean isShop(User user) throws Exception {
		
			// titleチェック
			if (!StringUtils.hasLength(user.title))
				return false;
			// tankaチェック
			if (user.tanka < 500)
				return false;
			return true;
		
	}
	
	/**
	 * 出品情報登録
	 * @param user
	 * @param imageFile
	 * @param jdbcTemplate
	 * @throws Exception
	 */
	public void doProfileShop(User user, MultipartFile imageFile, JdbcTemplate jdbcTemplate) throws Exception {
		
		// 画像変更した場合
		if (StringUtils.hasLength(user.imageFile)) {
			imageService.saveImageFile(user, imageFile);
			// 出品情報更新
			tUser.updateProfileShop(user, jdbcTemplate);
		} else {
			// 出品情報更新
			tUser.updateProfileShopNoImage(user, jdbcTemplate);			
		}
		
		
	}
}
