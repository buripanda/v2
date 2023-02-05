package com.v2.bean;

import java.time.LocalDateTime;
import java.util.Date;

import com.v2.util.DateUtil;

public class Recruit extends Error {
	
	public int id;
	public LocalDateTime recruitStartDate;
	public LocalDateTime recruitEndDate;
	public String message;
	public int recruitFlg;
	public int stopFlg;
	public int nowFlg;
	public String deleteFlg;
	public Date updateDate;
	public Date registDate;
	public String userName;
	public int price;
	public String imageFile;
	public String getRecruitStartDate() {
		return DateUtil.changeDateString(recruitStartDate);
	}
	public String getRecruitEndDate() {
		return DateUtil.changeDateString(recruitEndDate);
	}
  public String getHope() {
    if (recruitFlg == 0 || recruitFlg == 1) 
      return "出品";
    return "購入";
  }
	public boolean isAgree() {
	  if (nowFlg == 1) 
	    return true;
	  return false;
	}
  public boolean isRecruitFlg1() {
    if (recruitFlg == 0 || recruitFlg == 1) 
      return true;
    return false;
  }
  public boolean isRecruitFlg2() {
    if (recruitFlg == 2) 
      return true;
    return false;
  }
  public String reserveTime() {
    if (nowFlg == 1)
      return "今すぐ遊べます";
    return DateUtil.changeDateString(recruitStartDate) 
        + "　～　" 
        + DateUtil.changeDateString(recruitEndDate);
  }
}
