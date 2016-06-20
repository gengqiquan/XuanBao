package com.aibaide.xuanbao.bean;

import java.io.Serializable;

public class MsgBean implements Serializable{
	private String id;
	private String msg_content;
	private String rt;
	private Long read_state;
	private Long msg_biss;
	private String file_url;
	private String msg_biss_id;
	public String getMsg_content() {
		return msg_content;
	}
	public void setMsg_content(String msg_content) {
		this.msg_content = msg_content;
	}
	public String getRt() {
		return rt;
	}
	public void setRt(String rt) {
		this.rt = rt;
	}
	public Long getRead_state() {
		return read_state;
	}
	public void setRead_state(Long read_state) {
		this.read_state = read_state;
	}
	public String getFile_url() {
		return file_url;
	}
	public void setFile_url(String file_url) {
		this.file_url = file_url;
	}
	public String getMsg_biss_id() {
		return msg_biss_id;
	}
	public void setMsg_biss_id(String msg_biss_id) {
		this.msg_biss_id = msg_biss_id;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getMsg_biss() {
		return msg_biss;
	}
	public void setMsg_biss(Long msg_biss) {
		this.msg_biss = msg_biss;
	}
}
