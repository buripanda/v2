package com.v2.service;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.v2.bean.Chat;
import com.v2.bean.ReserveHist;
import com.v2.bean.User;

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
				//sb.append("<div class=\"partner_img\">");
				//sb.append("<img src=\"/getImgMini?id=").append(chat.sendId).append("&name=").append(chat.sendImagePath).append("\">");
				//sb.append("</div>");
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
	
	/**
	 * プロフィール情報を構築
	 * @param id
	 * @param chatList
	 * @return
	 * @throws Exception
	 */
	public String getProfile(User user) throws Exception {
		
		StringBuffer sb = new StringBuffer();
		//sb.append("<div class=\"profile_body\" id=\"profile_body\">");
		sb.append("<div class=\"profile_body_inner\">");
		sb.append("<label class=\"image_select\">");
		sb.append("<img src=\"/getImgDefault?id=").append(user.id).append("&name=").append(user.imageFile).append("\">");
		sb.append("</label>");
		sb.append("<div class=\"username\">").append(user.userName).append("</div>");
		if (user.price != 0) {
			sb.append("<div class=\"very_frame\">");
			sb.append("<div class=\"very_price\" id=\"very_price\">").append(user.price).append("</div><div class=\"very_tani\">Very</div>");
			sb.append("</div>");
		}
		sb.append("</div>");
		//sb.append("<div class=\"reserve-open reserve_div\" id=\"reserve_div\">");
		//sb.append("<a class=\"reserve_button reserve_pushdown\"><span>日程を予約する</span></a>");
		//sb.append("</div>");
		
		return sb.toString();
		
	}

	 /**
   * 予約一覧を構築（購入者）
   * @param id
   * @param chatList
   * @return
   * @throws Exception
   */
  public String getReserveList(List<ReserveHist> reserveHist, int id) throws Exception {
    
    StringBuffer sb = new StringBuffer();
    DateTimeFormatter datetimeformatter = DateTimeFormatter.ofPattern("MM月dd日HH:mm");
    int totalm = 0;
    int hh = 0;
    int mm = 0;
    sb.append("<div class=\"reserve_title\">予約/オーダー一覧</div>");
    for (ReserveHist reserve : reserveHist) {
    	
      if (reserve.buysellFlg == 1 && id == reserve.buyerId) {
    	  //購入分
    	  sb.append("<div class=\"reserve_content\" onclick=\"javascript:rate_open(")
    	  .append(reserve.reserveId).append(",").append(reserve.sellerId).append(",").append("1)\">");
    	  //sb.append("<div class=\"reserve_content rate-open\">");
	      sb.append("<img src=\"/image/pin_blue.png\" class=\"reserve_img\">");
	      sb.append("<div class=\"reserve_text\">");
	      sb.append("予約<br>");
	      String datetimeformated = datetimeformatter.format(reserve.reserveStartDate);
	      sb.append(datetimeformated).append("開始（");
	      
	      totalm = reserve.quantity * 15;
	      hh = totalm / 60;
	      mm = totalm - (hh * 60);
	      sb.append(hh).append("時間");
	      if (mm > 0)
	        sb.append(totalm).append("分");
	      
	      sb.append("）");
	      sb.append("</div></div>");
        sb.append("<input type=\"hidden\" name=\"reserve_list_pid\" value=\"").append(reserve.buyerId).append("\" id=\"reserve_list_pid\">");
        sb.append("<input type=\"hidden\" name=\"reserve_list_flg\" value=\"1\" id=\"reserve_list_flg\">");
      }
      if (reserve.buysellFlg == 2 && id == reserve.sellerId) {
    	  //オーダー分
        sb.append("<div class=\"reserve_content reserve_irai\" onclick=\"javascript:rate_open(")
        .append(reserve.reserveId).append(",").append(reserve.buyerId).append(",").append("2)\">");
          //sb.append("<div class=\"reserve_content reserve_irai rate-open\">");
       	  //sb.append("<img src=\"/getImgMini?id=").append(reserve.id).append("&name=").append(reserve.imageFile).append("\">");
       	  sb.append("<img src=\"/image/pin_blue.png\" class=\"reserve_img\">");
          sb.append("<div class=\"reserve_text\">");
          sb.append("オーダー　");
          sb.append(reserve.userName);
          sb.append("<br>");
          String datetimeformated = datetimeformatter.format(reserve.reserveStartDate);
          sb.append(datetimeformated).append("開始（");
          
          totalm = reserve.quantity * 15;
          hh = totalm / 60;
          mm = totalm - (hh * 60);
          sb.append(hh).append("時間");
          if (mm > 0)
            sb.append(totalm).append("分");
          
          sb.append("）");
          sb.append("</div></div>");
          sb.append("<input type=\"hidden\" name=\"reserve_list_pid\" value=\"").append(reserve.sellerId).append("\" id=\"reserve_list_pid\">");
          sb.append("<input type=\"hidden\" name=\"reserve_list_flg\" value=\"2\" id=\"reserve_list_flg\">");
      }
    }
    sb.append("<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js\"></script>");
    sb.append("<script src=\"/js/message.js\"></script>");
    return sb.toString();
    
  }

}
