package com.v2.controller.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.v2.bean.ChargeHist;
import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.ChargeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
public class ChargeController extends AbstractController {

	@Autowired
	ChargeService chargeService;
	
	private final JdbcTemplate jdbcTemplate;

	public ChargeController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 *  課金画面表示（Get）
	 */
	@GetMapping("/chargeView")
	public String chargeViewG(ModelMap modelMap) {

		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";
		return "charge";
		
	}

	/**
	 *  課金処理（Get)
	 */
	@PostMapping("/chargeView")
	public String chargeView(ModelMap modelMap) {
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";
		return "charge";	
	}

	/**
	 *  課金処理（Get)
	 */
	@GetMapping("/charge")
	public String chargeG(ModelMap modelMap) {

		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";
		
		// pidを渡して画面表示
		return this.charge(0, modelMap);
		
	}
	/**
	 *  課金処理
	 */
	@PostMapping("/charge")
	public String charge(@RequestParam(name="amount") int amount, ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";		
		User user = super.getSessionBean();
		
		ChargeHist charge = new ChargeHist();
		charge.fromId = user.id;
		charge.toId = user.id;
		charge.chargeKbn = 1;
		charge.chargeRadio = 1;
		charge.quantity = 1;
		charge.amount =  amount;
		//入力値チェック
		if (!chargeService.doCheck(charge)) 
			return "redirect:/";		
		
		// 課金処理
		try {
			user = chargeService.doCharge(charge, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		// セッションにユーザ情報格納
		super.setSessionBean(user);
		return "charge";
		
	}
}
