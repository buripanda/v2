package com.v2.bean;

import lombok.Data;

@Data
public class SessionBean {
    /** 自分のID */
    public int id;
    /** 相手のID　 */
    public int pid;
    /** 相手の名前 */
    public String pname;
}
