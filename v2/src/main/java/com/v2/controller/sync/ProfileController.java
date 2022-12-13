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
	 *  プロフィール画面表示（Get)
	 */
	@GetMapping("/profileView")
	public String profileViewG(ModelMap modelMap) {

		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";
		
		// pidを渡して画面表示
		User user = super.getSessionBean();
		int pid = super.getSessionBeanInt("pid");
		return this.profileView(pid, modelMap);
		
	}
	/**
	 *  プロフィール画面表示
	 */
	@PostMapping("/profileView")
	public String profileView(@RequestParam(name="pid", defaultValue = "0") int pid, ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";
		
		User user = super.getSessionBean();
		int searchId = user.id;
		if (pid > 0) 
			searchId = pid;
		
		// プロフィール取得
		try {
			user = profileService.getProfile(searchId, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		
		// 相手IDを保存しておく
		super.setSessionBeanInt("pid", pid);
		// pidが0なら自分のIDをセット
		if (pid == 0)
			super.setSessionBeanInt("pid", user.id);
		super.setSessionBean(user);
		modelMap.addAttribute("user", user);
		return "profile";
		
	}
	
	/**
	 *  プロフィール変更画面表示（Get)
	 */
	@GetMapping("/profileEdit")
	public String profileEditG(ModelMap modelMap) {

		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";
		
		return this.profileEdit(modelMap);
		
	}

	/**
	 *  プロフィール変更画面(表示)
	 */
	@PostMapping("/profileEdit")
	public String profileEdit(ModelMap modelMap) {
		
		// セッションを取得
		if (!super.isLogin())
			return "redirect:/";
		
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
	@PostMapping("/profileEditRegist")
	public String profileEditRegist(@RequestParam("user_name") String userName,
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
  			return "redirect:/";
  		
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
  			return "forward:profileEdit";
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
}
