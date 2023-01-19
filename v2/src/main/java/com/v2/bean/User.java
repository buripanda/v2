package com.v2.bean;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.Data;

@Data
public class User {
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
    
    public String getRegistDateYMD() {
    	return new SimpleDateFormat("yyyy年MM月dd日").format(registDate);
    }
    public String getAmountKanma() {
      return String.format("%,d", amount);
    }
    public String getPriceKanma() {
      return String.format("%,d", price);
    }    
}
