package com.v2.controller.sync;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.v2.bean.User;
import com.v2.controller.AbstractController;
import com.v2.service.ImageService;
import com.v2.service.IndexService;
import com.v2.service.MessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@Scope("request")
public class IndexController extends AbstractController {

	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	MessageService chatService;
	
	@Autowired
	IndexService indexService;
	
	@Autowired
	ImageService imageService;


	/**
	 * コンストラクタ
	 * @param jdbcTemplate
	 */
	public IndexController(JdbcTemplate jdbcTemplate) {

		this.jdbcTemplate = jdbcTemplate;

	}

	/**
	 * トップページ表示
	 * @param modelMap
	 * @return
	 */
	@GetMapping("/")
	public String indexGet(ModelMap modelMap) {
		
		// プロフィール一覧を取得する
		try {
			// 古い順
			List<User> userList =indexService.getProfileListOderRegist(jdbcTemplate);
			modelMap.addAttribute("userList", userList);
			// 新着順
			List<User> newUserList =indexService.getProfileListOderRegistDesc(jdbcTemplate);
			modelMap.addAttribute("newUserList", newUserList);
			// オーダーの多い順
			List<User> orderSumList = indexService.getProfileListOderSum(jdbcTemplate);
			modelMap.addAttribute("orderSumList", orderSumList);
  		
			// ログイン中でない場合は空で作成する
			if (!super.isLogin()) {
				User user = new User();
				super.setSessionBean(user);
			}
  		
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "index";
		
	}
	
	/**
	 * トップページ表示
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/index")
	public String indexPost(ModelMap modelMap) {

		// プロフィール一覧を取得する
		try {
			// 古い順
			List<User> userList =indexService.getProfileListOderRegist(jdbcTemplate);
			modelMap.addAttribute("userList", userList);
			// 新着順
			List<User> newUserList =indexService.getProfileListOderRegistDesc(jdbcTemplate);
			modelMap.addAttribute("newUserList", newUserList);
			// オーダーの多い順
			List<User> orderSumList =indexService.getProfileListOderSum(jdbcTemplate);
			modelMap.addAttribute("orderSumList", orderSumList);

			// ログイン中でない場合は空で作成する
			if (!super.isLogin()) {
				User user = new User();
				super.setSessionBean(user);
			}

		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "index";
	}

	/**
	 * キーワード検索処理（Get）
	 * @param modelMap
	 * @return
	 */
	@GetMapping("/keyword")
	public String keywordGet(@RequestParam(name="keyword", required = false) String keyword, ModelMap modelMap) {
		return keywordPost(keyword, modelMap);
	}
	/**
	 * キーワード検索処理
	 * @param modelMap
	 * @return
	 */
	@PostMapping("/keyword")
	public String keywordPost(@RequestParam(name="keyword", required = false) String keyword, ModelMap modelMap) {
		
		// プロフィール一覧を取得する
		try {
			// 古い順
			List<User> userList =indexService.getProfileListKeyword(keyword, jdbcTemplate);
  		modelMap.addAttribute("userList", userList);
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "search_result";
	}

	/**
	 * 画像データ取得（ミニ）
	 * @param id
	 * @param fileName
	 * @return
	 */
	@RequestMapping("/getImgMini")
	@ResponseBody
	public HttpEntity<byte[]> getImgMini(@RequestParam("id") int id,
			@RequestParam("name") String fileName) {
		
		byte[] byteImg = null;
		HttpHeaders headers = null;
		try {
			//バイト列に変換
			byteImg = imageService.getFileBytesMini(id, fileName);
			headers = new HttpHeaders();

			//Responseのヘッダーを作成
			headers.setContentType(MediaType.IMAGE_PNG);
			headers.setContentLength(byteImg.length);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return new HttpEntity<byte[]>(byteImg, headers);
	}

	/**
	 * 画像データ取得
	 * @param id
	 * @param fileName
	 * @return
	 */
	@RequestMapping("/getImgDefault")
	@ResponseBody
	public HttpEntity<byte[]> getImgDefault(@RequestParam("id") int id,
			@RequestParam("name") String fileName) {
		
		byte[] byteImg = null;
		HttpHeaders headers = null;
		try {
			//バイト列に変換
			byteImg = imageService.getFileBytes(id, fileName);
			headers = new HttpHeaders();

			//Responseのヘッダーを作成
			headers.setContentType(MediaType.IMAGE_PNG);
			headers.setContentLength(byteImg.length);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return new HttpEntity<byte[]>(byteImg, headers);
	}

	/**
	 * 画像データ取得（共通）
	 * @param id
	 * @param fileName
	 * @return
	 */
	@RequestMapping("/getImgCommon")
	@ResponseBody
	public HttpEntity<byte[]> getImgCommon(@RequestParam("name") String fileName) {
		
		byte[] byteImg = null;
		HttpHeaders headers = null;
		try {
			//バイト列に変換
			byteImg = imageService.getFileBytesCommon(fileName);
			headers = new HttpHeaders();

			//Responseのヘッダーを作成
			headers.setContentType(MediaType.IMAGE_PNG);
			headers.setContentLength(byteImg.length);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return new HttpEntity<byte[]>(byteImg, headers);
	}
	
	/**
	 * 評価ページ表示（仮）
	 * @param modelMap
	 * @return
	 */
	@GetMapping("/test")
	public String starGet(ModelMap modelMap) {
		return "test";
	}

}
