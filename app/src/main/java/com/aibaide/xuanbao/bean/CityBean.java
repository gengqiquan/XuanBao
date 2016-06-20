package com.aibaide.xuanbao.bean;

public class CityBean {
	private int _id;
	private String address_id;
	private String addr_type;
	private String address_code;
	private String father_id;
	private String create_name;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getAddress_id() {
		return address_id;
	}

	public void setAddress_id(String address_id) {
		this.address_id = address_id;
	}

	public String getAddr_type() {
		return addr_type;
	}

	public void setAddr_type(String addr_type) {
		this.addr_type = addr_type;
	}

	public String getAddress_code() {
		return address_code;
	}

	public void setAddress_code(String address_code) {
		this.address_code = address_code;
	}

	public String getFather_id() {
		return father_id;
	}

	public void setFather_id(String father_id) {
		this.father_id = father_id;
	}

	public String getCreate_name() {
		return create_name;
	}

	public void setCreate_name(String create_name) {
		this.create_name = create_name;
	}

}
