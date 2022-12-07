package com.v2.controller;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.v2.bean.SessionBean;

public class AbstractController {
	
	@Autowired
	HttpSession session;
	
	/**
	 * ログイン中か確認する
	 * @return
	 */
	protected boolean isLogin() {
		
		Object obj = session.getAttribute("v2bean");
		if (obj == null)
			return false;
		
		if (((SessionBean)obj).id == 0)
			return false;
		
		return true;
		
	}
	
	/**
	 * セッションBeanを取得する
	 * @return
	 */
	protected SessionBean getSessionBean() {

		Object obj = session.getAttribute("v2bean");
		if (obj == null)
			return new SessionBean();
		
		return (SessionBean)obj;
	
	}

	/**
	 * セッションBeanを設定する
	 * @return
	 */
	protected void setSessionBean(SessionBean bean) {
		
		session.setAttribute("v2bean", bean);
	
	}

	
	/**
	 * セッションを破棄する
	 * @return
	 */
	protected void removeSession() {

		session.invalidate();
	
	}
	
	/**
	 * 空文字の場合はnullに変換する
	 * @param str
	 * @return
	 */
	public String chgNull(String str) {
		
		if (!StringUtils.hasLength(str))
			return null;
		return str;
	
	}
	
	/**
	 * 現在日時からハッシュ値を生成する
	 * @return
	 * @throws Exception
	 */
	protected String getCookieHash() throws Exception {
		
		// Cookie用にハッシュ値を生成する
		Date date = new Date();
		String str = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(date);
		MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
		byte[] sha256_result = sha256.digest(str.getBytes());
		String hashStr = String.format("%040x", new BigInteger(1, sha256_result));
		System.out.println("SHA-256：" + hashStr);
		return hashStr;
		
	}
}
