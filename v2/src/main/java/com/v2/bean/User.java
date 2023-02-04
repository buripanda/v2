package com.v2.bean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import com.v2.util.GlobalConst;

import lombok.Data;

@Data
public class User extends Error {
    /** ID */
    public int id;
    /** ユーザID　 */
    public String userId;
    /** ユーザ名 */
    public String userName;
    /** パスワード */
    public String password;
    /** タイトル */
    public String title;
    /** メッセージ */
    public String message;
    /** イメージ画像 */
    public String imageFile;
    /** Eメール */
    public String email;
    /** ログインID */    
    public int loginId;
    /** 性別 */
    public String sex;
    /** 性別チェック */
    public int sexChecked = 1;
    /** 単価 */
    public int price;
    /** 残高 */
    public int amount;
    /** 評価平均（出品者） */
    public BigDecimal rate;
    /** 評価合計（出品者） */
    public int rateSum;
    /** オーダー数（出品者） */
    public int orderSum;
    /** 評価平均（購入者） */
    public BigDecimal rateBuy;
    /** 評価合計（購入者） */
    public int rateSumBuy;
    /** オーダー数（購入者） */
    public int orderSumBuy;
    /** インスタ */
    public String insta;
    /** ツイッター */
    public String twitter;
    /** ユーチューブ */
    public String youtube;
    /** TIKTOK */
    public String tiktok;
    /** オンラインステータス */
    public int onlineStatus;
    /** 登録ステータス */
    public int registStatus;    
    /** Cookie */
    public String cookie;    
    /** ベル通知　 */
    public int bellTuchi;
    /** チャット通知　 */
    public int chatTuchi;
    /** チャット新着通知　 */
    public int newMessage;
    /** 更新日時 */
    public Date updateDate;
    /** 登録日時 */
    public Date registDate;
    /** 評価数 */
    public int rateCount;
    
    public String getRegistDateYMD() {
    	return new SimpleDateFormat("yyyy年MM月dd日").format(registDate);
    }
    public String getAmountKanma() {
      return String.format("%,d", amount);
    }
    public String getPriceKanma() {
      return String.format("%,d", price);
    }
    public String getCutMessage() {
    	String ret = "";
    	if (this.checkStamp(message))
    		return ret;
    	if (Objects.nonNull(message)) {
    		if (message.length() > 8)
    			ret = message.substring(0, 8);
    		else
    			ret = message;
    	}
    	return ret;
    }
    public String getCutTitle() {
      String ret = "";
      if (Objects.nonNull(title)) {
        if (title.length() > GlobalConst.TITILE_LENGTH)
          ret = title.substring(0, GlobalConst.TITILE_LENGTH - 1);
        else
          ret = title;
      }
      return ret;
    }
    
    public String getBadgeImg() {
    	String ret = "";
    	if (orderSum < 10)
    		ret = "badge_bronze.png";
    	else if (orderSum < 20)
    		ret = "badge_sapphire.png";
    	else if (orderSum < 30)
    		ret = "badge_silver.png";
    	else if (orderSum < 40)
    		ret = "badge_ruby.png";
    	else if (orderSum < 50)
    		ret = "badge_emerald.png";
    	else if (orderSum < 60)
    		ret = "badge_gold.png";
    	else if (orderSum < 70)
    		ret = "badge_purple_diamond.png";
    	else if (orderSum < 80)
    		ret = "badge_red_diamond.png";
    	else 
    		ret = "badge_legend_diamond.png";
    	return ret;
     }
    public String getBadgeTxt() {
    	String ret = "";
    	if (orderSum < 10)
    		ret = "購入回数10回未満";
    	else if (orderSum < 20)
    		ret = "購入回数10回以上達成";
    	else if (orderSum < 30)
    		ret = "購入回数20回以上達成";
    	else if (orderSum < 40)
    		ret = "購入回数30回以上達成";
    	else if (orderSum < 50)
    		ret = "購入回数40回以上達成";
    	else if (orderSum < 60)
    		ret = "購入回数50回以上達成";
    	else if (orderSum < 70)
    		ret = "購入回数60回以上達成";
    	else if (orderSum < 80)
    		ret = "購入回数70回以上達成";
    	else 
    		ret = "購入回数80回以上達成";
    	return ret;
     }
    
    /**
     * スタンプが送信されたかチェックする
     * @param message
     * @return
     */
    private boolean checkStamp(String message) {
  	  for (GlobalConst.Stamp stamp : GlobalConst.Stamp.values()) {
            if(stamp.getValue().equals(message)) {
          	  return true;
            }
         }
  	  return false;
    }
}
