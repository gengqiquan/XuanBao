package com.aibaide.xuanbao.bean;

public class VirtualWinsBean {
	private String nick_name;

	private String phone;
	private String file_url;

	private String take_time;

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public String getNick_name() {
		return this.nick_name;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setTake_time(String take_time) {
		this.take_time = take_time;
	}

	public String getTake_time() {
		return this.take_time;
	}

	public String getFile_url() {
		return file_url;
	}

	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}
}
