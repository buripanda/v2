package com.v2.controller.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.v2.bean.Recruit;
import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.ProfileService;
import com.v2.service.RecruitService;
import com.v2.util.DateUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
public class RecruitController extends AbstractController {
	
	@Autowired
	RecruitService recruitService;	
	
	@Autowired
	ProfileService profileService;
	
	private final JdbcTemplate jdbcTemplate;

	public RecruitController(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 募集画面表示
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@GetMapping("/recruitView")
	public String recruitViewG(ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";		
		return recruitView(modelMap);
	
	}
	
	/**
	 * 募集画面表示
	 * @param id
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/recruitView")
	public String recruitView(ModelMap modelMap) {
		
		// セッションからログインID取得
		if (!super.isLogin())
			return "redirect:/";		
		User user = super.getSessionBean();
		int id = user.id;
		
		Recruit recruit = new Recruit();
		Recruit param = new Recruit();
		try {
			// 募集メッセージ取得
			recruit = recruitService.viewRecruit(id, jdbcTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		modelMap.addAttribute("recruit", recruit);
    modelMap.addAttribute("form", param);
		return "recruit";
	}
  /**
   * 募集登録
   * @param id
   * @param modelMap
   * @return
   */
  @PostMapping("/recruitRegist")
  public String recruitRegist(
      @RequestParam("datetimefrom") String datetimefrom,
      @RequestParam("datetimeto") String datetimeto,
      @RequestParam("message") String message,
      @RequestParam(value="agree", required=false) String agree,
      @RequestParam(value="recruit_select", required=false) String recruitSelect,
      ModelMap modelMap) {
    
    // セッションからログインID取得
    if (!super.isLogin())
      return "redirect:/";    
    User user = super.getSessionBean();
    int id = user.id;
        
    Recruit recruit = new Recruit();
    Recruit param = new Recruit();
    try {      
      // パラメタチェック
      if (StringUtils.hasLength(datetimefrom))
        param.recruitStartDate = DateUtil.changeStringDate(datetimefrom);
      if (StringUtils.hasLength(datetimeto))
        param.recruitEndDate = DateUtil.changeStringDate( datetimeto);
      param.message = message;
      if (StringUtils.hasLength(agree))
        param.nowFlg = 1;
      else 
        param.nowFlg = 0;        
      if ("1".equals(recruitSelect))
        param.recruitFlg = 1;
      if ("2".equals(recruitSelect))
        param.recruitFlg = 2;        
      param.id = id;
      if (!recruitService.isParam(param)) {
        // 募集メッセージ取得
        recruit = recruitService.viewRecruit(id, jdbcTemplate);
        modelMap.addAttribute("recruit", recruit);
        modelMap.addAttribute("form", param);
        return "recruit";
      }
      // 募集登録
      recruitService.registRecruit(param, jdbcTemplate);
      // 募集メッセージ取得
      recruit = recruitService.viewRecruit(id, jdbcTemplate);
    } catch (Exception e) {
      e.printStackTrace();
      return "error";
    }
    modelMap.addAttribute("recruit", recruit);
    modelMap.addAttribute("form", param);
    return "recruit";
  }
  /**
   * 募集登録
   * @param id
   * @param modelMap
   * @return
   */
  @PostMapping("/recruitStop")
  public String recruitStop(ModelMap modelMap) {
    
    // セッションからログインID取得
    if (!super.isLogin())
      return "redirect:/";    
    User user = super.getSessionBean();
    int id = user.id;
        
    Recruit recruit = new Recruit();
    Recruit param = new Recruit();
    param.id = id;
    try {      
      // 募集削除
      recruitService.registStop(param, jdbcTemplate);
      // 募集メッセージ取得
      recruit = recruitService.viewRecruit(id, jdbcTemplate);
    } catch (Exception e) {
      e.printStackTrace();
      return "error";
    }
    modelMap.addAttribute("recruit", recruit);
    modelMap.addAttribute("form", param);
    return "recruit";
  }
}
