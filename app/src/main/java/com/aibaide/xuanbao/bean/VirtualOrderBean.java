package com.aibaide.xuanbao.bean;

public class VirtualOrderBean {
	private int take_exchange;

	private String take_time;

	private int virtual_price;

	private String code;

	private int scor;

	private String opt_content;

	private String virtual_name;

	private String file_url;

	private int virtual_details_id;

	public void setTake_exchange(int take_exchange) {
		this.take_exchange = take_exchange;
	}

	public int getTake_exchange() {
		return this.take_exchange;
	}

	public void setTake_time(String take_time) {
		this.take_time = take_time;
	}

	public String getTake_time() {
		return this.take_time;
	}

	public void setVirtual_price(int virtual_price) {
		this.virtual_price = virtual_price;
	}

	public int getVirtual_price() {
		return this.virtual_price;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return this.code;
	}

	public void setScor(int scor) {
		this.scor = scor;
	}

	public int getScor() {
		return this.scor;
	}

	public void setOpt_content(String opt_content) {
		this.opt_content = opt_content;
	}

	public String getOpt_content() {
		return this.opt_content;
	}

	public void setVirtual_name(String virtual_name) {
		this.virtual_name = virtual_name;
	}

	public String getVirtual_name() {
		return this.virtual_name;
	}

	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}

	public String getFile_url() {
		return this.file_url;
	}

	public void setVirtual_details_id(int virtual_details_id) {
		this.virtual_details_id = virtual_details_id;
	}

	public int getVirtual_details_id() {
		return this.virtual_details_id;
	}
}
