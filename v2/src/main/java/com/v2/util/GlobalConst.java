package com.v2.util;

public class GlobalConst {
	
	// チケット1枚あたりの基準時間
	public static final int reserveTime = 15;
	// タイトル表示文字数
	public static int TITILE_LENGTH = 15;
	
	 public static enum Stamp {
		  Stamp1("stamp1"),
		  Stamp2("stamp2"),
		  Stamp3("stamp3"),
		  Stamp4("stamp4"),
		  Stamp5("stamp5"),
		  Stamp6("stamp6");
		    
		  // フィールドの定義
		  private String stamp;
		    
		  // コンストラクタの定義
		  private Stamp(String stamp) {
			  this.stamp = stamp;
		  }
		  public String getValue() {
			  return this.stamp;
		  }
	  };

}
