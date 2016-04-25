package com.qtest.journal.model;

import java.io.Serializable;

public class TokenResponseBean implements Serializable {

	private int errcode;
	private String errmsg;
	private String token;

	private static final long serialVersionUID = 1L;
	public String getMsg() {
		return errmsg;
	}
	public void setMsg(String msg) {
		this.errmsg = msg;
	}
	
	public int getErrcode() {
		return errcode;
	}
	public void setErrcode(int errcode) {
		this.errcode = errcode;
	}
	
	public String getToken() {
		return token;
	}
	
	public void setToken(String token) {
		this.token = token;
	}
}
