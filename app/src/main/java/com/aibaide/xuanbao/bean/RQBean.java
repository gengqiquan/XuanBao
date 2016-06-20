package com.aibaide.xuanbao.bean;

public class RQBean {
	public RQBean() {
	}

	public String data;
	public String code;
	public int totalPage;
	public int totalResult;
	public boolean success;

	public String msg;

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return this.data;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public boolean getSuccess() {
		return this.success;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMsg() {
		return this.msg;
	}
}
