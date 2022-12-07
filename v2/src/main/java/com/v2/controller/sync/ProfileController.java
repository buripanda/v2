package com.v2.controller.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.v2.bean.SessionBean;
import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.ProfileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
public class ProfileController extends AbstractController {

	@Autowired
	ProfileService profileService;
	
	private final JdbcTemplate jdbcTemplate;

	public ProfileController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	@GetMapping("/purchase")
	public String purchaseGet(ModelMap modelMap) {
		
		return "purchase";
		
	}
	
	/**
	 *  プロフィール画面(表示)
	 */
	@GetMapping("/profile")
	public String profileGet(ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/login";
		
		super.getSessionBean();
		int id = super.getSessionBean().id;
		
		// プロフィール取得
		User user = new User();
		try {
			user = profileService.getProfile(id, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		modelMap.addAttribute("user", user);
		return "profile";
		
	}
	/**
	 *  プロフィール変更画面(表示)
	 */
	@PostMapping("/profile_edit")
	public String profileEditPost(ModelMap modelMap) {
		
		// セッションを取得
		if (!super.isLogin())
			return "redirect:/login";
		
		int id = super.getSessionBean().id;
		User user = new User();
		try {
			//プロフィール情報取得
			user = profileService.getProfile(id, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		modelMap.addAttribute("user", user);
		return "profile_edit";
		
	}
	/**
	 *  プロフィール変更画面(変更処理)
	 */
	@PostMapping("/profile_edit_regist")
	public String profileEditRegistPost(@RequestParam("user_name") String userName,
			@RequestParam("image_file") MultipartFile imageFile,
			@RequestParam("message") String message,
			@RequestParam("youtube") String youtube,
			@RequestParam("twitter") String twitter,
			@RequestParam("tiktok") String tiktok,
			@RequestParam("instagram") String instagram,
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
  		user.userName = chgNull(userName);
  		user.imageFile = imageFile.getOriginalFilename();
  		user.message = chgNull(message);
  		user.youtube = chgNull(youtube);
  		user.twitter = chgNull(twitter);
  		user.tiktok = chgNull(tiktok);
  		user.insta = chgNull(instagram);
  		// パラメタチェック
  		if (!profileService.isProfileEdit(user)) {
  			modelMap.addAttribute("user", user);
  			return "forward:profile_edit";
  		}
  		// プロフィール情報更新
  		profileService.doProfileModify(user, imageFile, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		modelMap.addAttribute("user", user);
		return "forward:/index";
		
	}

	/**
	 * 個人ページ表示
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@GetMapping("/view/{id}")
	public String viewGet(@PathVariable("id") int partnerId, ModelMap modelMap) {
		
		SessionBean bean = null;		
		// ログイン中か確認
		if (this.isLogin()) {
			bean = super.getSessionBean();
		} else {
			bean = new SessionBean();
		}
		
		// 参照先IDを保持
		bean.partnerId = partnerId;
		
		// 参照先のプロフィール取得
		User user = new User();
		try {
			user = profileService.getProfile(partnerId, jdbcTemplate);
  	} catch (Exception e) {
  		e.printStackTrace();
  		return "error";
  	}

		user.loginId = bean.id;
		modelMap.addAttribute("user", user);
		
		return "view";
	}

	/**
	 * 個人ページ表示
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/view")
	public String viewPost(@RequestParam("partnerId") int partnerId , ModelMap modelMap) {
		
		SessionBean bean = null;		
		// ログイン中か確認
		if (this.isLogin()) {
			bean = super.getSessionBean();
		} else {
			bean = new SessionBean();
		}
		
		// 参照先IDを保持
		bean.partnerId = partnerId;
		
		// 参照先のプロフィール取得
		User user = new User();
		try {
			user = profileService.getProfile(partnerId, jdbcTemplate);
  	} catch (Exception e) {
  		e.printStackTrace();
  		return "error";
  	}
		user.loginId = bean.id;
		modelMap.addAttribute("user", user);
		
		return "view";
		
	}

}
