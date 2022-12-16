package com.v2.bean;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ChargeHist {
	/** 課金ID */
	public int chargeId;
	/** 課金した人のID */
	public int  fromId;
	/** 課金された人のID */
	public int  toId;
	/** 課金した日 */
	public LocalDateTime  chargeDate;
	/** 課金区分 */
	public int   chargeKbn;
	/** 課金数 */
	public int   quantity;
	/** 金額選択 */
	public int  chargeRadio;
	/** 課金額 */
	public int  amount;
	/** 削除フラグ */
	public String  deleteFlg;
	/** 登録日 */
	public String  registDate;
	/** 更新日 */
	public String  updateDsate;
}
