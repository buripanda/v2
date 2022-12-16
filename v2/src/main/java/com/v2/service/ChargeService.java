package com.v2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.v2.bean.ChargeHist;
import com.v2.dao.ChargeHistDao;
import com.v2.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ChargeService {
	
	@Autowired
	ChargeHistDao chargeHistDao;
	
	@Autowired
	UserDao userDao;
	
	/**
	 * 課金処理
	 * @param user
	 * @param jdbcTemplate
	 * @return
	 * @throws Exception
	 */
	public void doCharge(ChargeHist chargeHist, JdbcTemplate jdbcTemplate) throws Exception {

		//シーケンス取得
		int seq = chargeHistDao.getSequenceChargeId(jdbcTemplate);
		chargeHist.chargeId = seq;
		//課金登録
		chargeHistDao.inserChargeHist(chargeHist, jdbcTemplate);
		// 残高更新
		userDao.updateBlance(chargeHist.toId, chargeHist.amount, jdbcTemplate);

	}
	
	/**
	 * 各値をチェック
	 * @param email
	 * @param password
	 * @param agree
	 * @return
	 */
	public boolean doCheck(ChargeHist chargeHist) {
		
		// userNameチェック
		if (chargeHist.chargeRadio < 0)
			return false;
		return true;

	}
	
}
