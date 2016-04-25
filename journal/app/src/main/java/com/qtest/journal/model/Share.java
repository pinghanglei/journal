package com.qtest.journal.model;

import java.io.Serializable;

public class Share implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;
	private String title;
	private String image;
	private String read_num;
	private String name;
	
	
	



	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}



	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}





	public String getImgurl() {
		return image;
	}
	public void setImgurl(String image) {
		this.image = image;
	}
	public String getReadNum() {
		return read_num;
	}
	public void setReadNum(String read_num) {
		this.read_num = read_num;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
