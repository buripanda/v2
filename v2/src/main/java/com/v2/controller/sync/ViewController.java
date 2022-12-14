package com.v2.controller.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.LoginService;
import com.v2.service.MessageService;
import com.v2.service.ProfileService;
import com.v2.service.SignupService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
public class ViewController extends AbstractController {

	
	@Autowired
	LoginService loginService;

	@Autowired
	ProfileService profileService;

	@Autowired
	MessageService chatService;
	
	@Autowired
	SignupService signupService;

	
	private final JdbcTemplate jdbcTemplate;

	public ViewController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	/**
	 * プロフィール新規登録（登録処理）
	 */
	@PostMapping("/add")
	public String addPost(@RequestParam("user_name") String userName,
			@RequestParam("image_file") MultipartFile imageFile,
			@RequestParam("message") String message) {
		
		//プロフィール登録
		int id = super.getSessionBean().id;
		User user = new User();
		user.id = id;
		user.userName = userName;
		user.message = message;
		try {
			//profileService.doDdd(user, imageFile, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "redirect:/"; // トップページにリダイレクト

	}

}
