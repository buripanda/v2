package com.v2.bean;

import java.time.LocalDateTime;
import java.util.Date;

import com.v2.util.DateUtil;

public class Recruit extends Error {
	
	public int id;
	public LocalDateTime recruitStartDate;
	public LocalDateTime recruitEndDate;
	public String message;
	public int stopFlg;
	public int nowFlg;
	public String deleteFlg;
	public Date updateDate;
	public Date registDate;
	public String getRecruitStartDate() {
		return DateUtil.changeDateString(recruitStartDate);
	}
	public String getRecruitEndDate() {
		return DateUtil.changeDateString(recruitEndDate);
	}
	public boolean isAgree() {
	  if (nowFlg == 1) 
	    return true;
	  return false;
	}
}
