package com.v2.controller.sync;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.v2.bean.ReserveHist;
import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.ReserveService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
public class SchejuleController extends AbstractController {
	
  private final ReserveService reserveService;
  private final JdbcTemplate jdbcTemplate;

  public SchejuleController(JdbcTemplate jdbcTemplate, ReserveService reserveService) {
	  this.jdbcTemplate = jdbcTemplate;
	  this.reserveService = reserveService;
  }

	/**
	 * 予約一覧
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@GetMapping("/schejuleView")
	public String schejuleViewG(ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";		
		return schejuleView(modelMap);
	
	}
	
	/**
	 * 予約一覧
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/schejuleView")
	public String schejuleView(ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";		
		User user = super.getSessionBean();
		int id = user.id;
		
		List<ReserveHist> schejuleList = new ArrayList<>();
    List<ReserveHist> schejuleCompList = new ArrayList<>();		
		try {
			// 予約一覧検索（未実施）
		  schejuleList = reserveService.getReserveListBuyAll(id, jdbcTemplate);
			modelMap.addAttribute("schejuleList", schejuleList);
      // 予約一覧検索（終了済）			
			schejuleCompList = reserveService.getReserveListBuyCompAll(id, jdbcTemplate);
			modelMap.addAttribute("schejuleCompList", schejuleCompList);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}		
		return "schejule";
	}
}
