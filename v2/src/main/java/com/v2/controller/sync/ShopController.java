package com.v2.controller.sync;

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
	private final UserDao tUser;
	private final ShopService shopService;

	public ShopController(JdbcTemplate jdbcTemplate, UserDao tUser, ShopService shopService) {
		this.jdbcTemplate = jdbcTemplate;
		this.tUser = tUser;
		this.shopService = shopService;
	}

	/**
	 *  出品ページ（表示）GET
	 */
	@GetMapping("/shopView")
	public String shopViewG(ModelMap modelMap) {
				
		// ログイン中か確認
		if (!super.isLogin())
			return "redirect:/";  		
		shopView(modelMap);
		return "shop";
		
	}


	/**
	 *  出品ページ（表示）
	 */
	@PostMapping("/shopView")
	public String shopView(ModelMap modelMap) {
				
		User user = new User();
		try {
  		// ログイン中か確認
  		if (!super.isLogin())
  			return "redirect:/";  		
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
	public String shopRegist(
			@RequestParam("image_file") MultipartFile imageFile,
			@RequestParam("title") String title,
			@RequestParam("price") String price,
			ModelMap modelMap) {
				
		User user = new User();

		try {
			
  		// ログイン中か確認
  		if (!super.isLogin())
  			return "redirect:/";
  		//セッションからユーザID取得
  		super.getSessionBean();
  		
  		User param = new User();
  		//パラメタ格納
  		param.id = super.getSessionBean().id;
  		param.imageFile = imageFile.getOriginalFilename();
  		param.title = chgNull(title);
  		if (price.isEmpty())
  		  param.price = 0;
  		else
  		  param.price = Integer.parseInt(price);  		  
  		// パラメタチェック
  		if (!shopService.isShop(param)) {
        //プロフィール取得
        user = tUser.selectUser(param.id, jdbcTemplate);
        user.title = param.title;
        user.price = param.price;
        user.errorMessage = param.errorMessage;
  			modelMap.addAttribute("user", user);
  			return "shop";
  		}
  		// プロフィール情報更新
  		shopService.doProfileShop(param, imageFile, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		modelMap.addAttribute("user", user);
		return "forward:/index";
		
	}


}
