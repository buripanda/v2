package com.v2.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.v2.bean.Chat;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HtmlService {
	
	public String getChatList(int id, List<Chat> chatList) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		String tmp = null;
		for (Chat chat : chatList) {
			
			// 自分が送信者でない場合
			if (id != chat.sendId) {

				sb.append("<div class=\"partner_chat\">");
				sb.append("<div class=\"partner_img\">");
				sb.append("<img src=\"/getImgMini?id=").append(chat.sendId).append("&name=").append(chat.sendImagePath).append("\">");
				sb.append("</div>");
				sb.append("<div class=\"chatting\">");
				sb.append("<div class=\"says\">");
				tmp = chat.message.replaceAll("\r\n|\r|\n", "<BR>");
				sb.append("<div>").append(tmp).append("</div>");
				sb.append("</div>");
				sb.append("</div>");
				sb.append("<div class=\"partner_time\">").append(chat.registDate).append("</div>");
				sb.append("</div>");
				
			// 自分が送信者の場合
			} else if (id == chat.sendId) {
				sb.append("<div class=\"mycomment\">");
				tmp = chat.message.replaceAll("\r\n|\r|\n", "<BR>");
				sb.append("<div>").append(tmp).append("</div>");
				sb.append("</div>");
				sb.append("<div class=\"my_time\">").append(chat.registDate).append("</div>");
			}
		}
		return sb.toString();
	}
}
