package com.v2.controller.sync;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.IndexService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@Scope("request")
public class SessionController extends AbstractController {

	private final JdbcTemplate jdbcTemplate;
	private final IndexService indexService;

	/**
	 * コンストラクタ
	 * @param jdbcTemplate
	 */
	public SessionController(JdbcTemplate jdbcTemplate, IndexService indexService) {
		this.jdbcTemplate = jdbcTemplate;
		this.indexService = indexService;
	}

	/**
	 * トップページ表示
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/leave")
	public void leavePost() {

		System.out.println("ページを離れました。");
    // ブラウザを離れた場合
		if (super.isLogin()) {
			User user = super.getSessionBean();
			System.out.println("ID=" + user.id);
		}
	}	
	

}
