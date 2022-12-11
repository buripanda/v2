package com.v2.bean;

import lombok.Data;

/**
 * WebSocket用Bean
 * @author burip
 *
 */
@Data
public class WsMessage {

	/* 送信先ID */
	public String sendId;
	/* 送信元ID */
	public String distId;
	/* モード（0：チャットメッセージ　1：ログイン */	
	public String mode;
	/* メッセージ */
	public String message;

}