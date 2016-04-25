
package com.qtest.journal.model;

import java.io.Serializable;


public class Result<T> implements Serializable  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int error;
	private T data;
	private String msg;
	private String  havenext;
	private int l;
	private int s;
	
	public int getErr() {
		return error;
	}
	public void setErr(int error) {
		this.error = error;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getErrMsg() {
		return msg ;
	}
	public void getErrMsg(String msg) {
		this.msg = msg;
	}
	public String getMore() {
		return havenext;
	}

	public void setMore(String havenext) {
		this.havenext = havenext;
	}
	public int getL() {
		return l;
	}
	public void setL(int l) {
		this.l = l;
	}
	public int getS() {
		return s;
	}
	public void setS(int s) {
		this.s = s;
	}
	
}
