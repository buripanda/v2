package com.v2.controller.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.dao.UserDao;
import com.v2.service.ShopService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
public class ShopController extends AbstractController {
	
	private final JdbcTemplate jdbcTemplate;

	public ShopController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@Autowired
	UserDao tUser;
	
	@Autowired
	ShopService shopService;
	
	/**
	 *  出品ページ（表示）
	 */
	@GetMapping("/shop")
	public String profileEditRegistGet(ModelMap modelMap) {
				
		User user = new User();

		try {
  		// ログイン中か確認
  		if (!super.isLogin())
  			return "redirect:/login";  		
  		//セッションからユーザID取得
  		int id = super.getSessionBean().id;
  		//プロフィール取得
  		user = tUser.selectUser(id, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		modelMap.addAttribute("user", user);
		return "shop";
		
	}

	/**
	 *  出品ページ(登録）
	 */
	@PostMapping("/shop_regist")
	public String shopRegistPost(
			@RequestParam("image_file") MultipartFile imageFile,
			@RequestParam("title") String title,
			@RequestParam("tanka") int tanka,
			ModelMap modelMap) {
				
		User user = new User();

		try {
			
  		// ログイン中か確認
  		if (!super.isLogin())
  			return "redirect:/login";
  		
  		//セッションからユーザID取得
  		super.getSessionBean();
  		
  		//パラメタ格納
			user.id = super.getSessionBean().id;
  		user.imageFile = imageFile.getOriginalFilename();
  		user.title = chgNull(title);
  		user.tanka = tanka;
  		// パラメタチェック
  		if (!shopService.isShop(user)) {
  			modelMap.addAttribute("user", user);
  			return "shop";
  		}
  		// プロフィール情報更新
  		shopService.doProfileShop(user, imageFile, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		modelMap.addAttribute("user", user);
		return "forward:/index";
		
	}


}
