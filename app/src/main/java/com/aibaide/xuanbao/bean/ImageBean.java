package com.aibaide.xuanbao.bean;

import java.io.Serializable;

public class ImageBean implements Serializable {
	private String imgurl;
	private String weburl;
	private String title;
	public static int NORESOUSE = -1;
	private int resID = NORESOUSE;
	private Long forid;
	private int fileBelong;
	private String EID;
	private String ext;
	private String body;

	public String getEID() {
		return EID;
	}

	public void setEID(String eID) {
		EID = eID;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	public String getWeburl() {
		return weburl;
	}

	public void setWeburl(String weburl) {
		this.weburl = weburl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getResID() {
		return resID;
	}

	public void setResID(int resID) {
		this.resID = resID;
	}

	public Long getForid() {
		return forid;
	}

	public void setForid(Long forid) {
		this.forid = forid;
	}

	public int getFileBelong() {
		return fileBelong;
	}

	public void setFileBelong(int fileBelong) {
		this.fileBelong = fileBelong;
	}

}
