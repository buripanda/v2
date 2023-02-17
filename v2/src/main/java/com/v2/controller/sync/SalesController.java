package com.v2.controller.sync;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.v2.bean.Sales;
import com.v2.bean.SalesHist;
import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.SalesService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
public class SalesController extends AbstractController {
	
	private final SalesService salesService;	

	public SalesController(JdbcTemplate jdbcTemplate, SalesService salesService) {
		this.salesService = salesService;
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
		
		List<SalesHist> salesList = new ArrayList<>();
		Sales sales = new Sales();
		try {
			// 売上を取得
			sales = salesService.getSalesSum(id);
			// 売上一覧取得
			salesList = salesService.getSalesList(id);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		modelMap.addAttribute("sales", sales);
		modelMap.addAttribute("salesList", salesList);
		return "sales";
	} 
}
