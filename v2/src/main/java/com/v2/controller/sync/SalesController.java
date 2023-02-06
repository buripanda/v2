package com.v2.controller.sync;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.v2.bean.Sales;
import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.SalesService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
public class SalesController extends AbstractController {
	
	@Autowired
	SalesService salesService;	
	
	private final JdbcTemplate jdbcTemplate;

	public SalesController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 売上管理表示
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@GetMapping("/salesView")
	public String salesViewG(ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";		
		return salesView(modelMap);
	
	}
	
	/**
	 * 売上管理表示
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/salesView")
	public String salesView(ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";		
		User user = super.getSessionBean();
		int id = user.id;
		
		List<Sales> sales = new ArrayList<>();
		User userSalesSum = new User();
		try {
			// 売上を取得
			userSalesSum = salesService.getSalesSum(id, jdbcTemplate);
			// 売上一覧取得
			sales = salesService.getSalesList(id, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		modelMap.addAttribute("user", userSalesSum);
    modelMap.addAttribute("sales", sales);
		return "sales";
	} 
}
