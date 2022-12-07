package com.v2.bean;

import lombok.Data;

@Data
public class Chat {
    /** 送信者ID */
    public int sendId;
    /** 受信者ID　 */
    public int receiveId;
    /** ログインID　 */
    public int loginId;
    /** 送信者の名前 */
    public String sendName;
    /** 受信者の名前 */
    public String receiveName;
    /** メッセージ */
    public String message;
    /** 送信者の画像パス*/
    public String sendImagePath;    
    /** 受信者の画像パス*/
    public String receiveImagePath;    
    /** 更新日時 */
    public String updateDate;
    /** 登録日時 */
    public String registDate;
}
