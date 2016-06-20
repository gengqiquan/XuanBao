package com.aibaide.xuanbao.bean;

public class WinUserBean {
	private Long phone;


	private String file_url;
	private String take_time;
	private String head_img_url;

	private String nick_name;


	public Long getPhone() {
		return phone;
	}


	public void setPhone(Long phone) {
		this.phone = phone;
	}


	public String getTake_time() {
		return take_time;
	}


	public void setTake_time(String take_time) {
		this.take_time = take_time;
	}


	public String getHead_img_url() {
		return head_img_url;
	}


	public void setHead_img_url(String head_img_url) {
		this.head_img_url = head_img_url;
	}


	public String getNick_name() {
		return nick_name;
	}


	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}


	public String getRt() {
		return rt;
	}


	public void setRt(String rt) {
		this.rt = rt;
	}


	private String rt;


	public String getFile_url() {
		return file_url;
	}


	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}
}
