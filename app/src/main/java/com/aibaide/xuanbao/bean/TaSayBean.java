package com.aibaide.xuanbao.bean;

import java.io.Serializable;
import java.util.List;

public class TaSayBean implements Serializable {
	private int comment_count;

	private int click_count;
	private Long collect_id;

	private String phone;
	private String headimg;
	private String nick_name;
	public boolean chedked;
	private List<Say_content> say_content;

	private int line_id;
	private int iscollect;
	private int ispraise;
	private int share_count;

	private String goods_name;

	private int member_id;

	private int say_id;

	private String saytime;

	private int praise_count;

	public void setComment_count(int comment_count) {
		this.comment_count = comment_count;
	}

	public int getComment_count() {
		return this.comment_count;
	}

	public void setClick_count(int click_count) {
		this.click_count = click_count;
	}

	public int getClick_count() {
		return this.click_count;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setSay_content(List<Say_content> say_content) {
		this.say_content = say_content;
	}

	public List<Say_content> getSay_content() {
		return this.say_content;
	}

	public void setLine_id(int line_id) {
		this.line_id = line_id;
	}

	public int getLine_id() {
		return this.line_id;
	}

	public void setShare_count(int share_count) {
		this.share_count = share_count;
	}

	public int getShare_count() {
		return this.share_count;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_name() {
		return this.goods_name;
	}

	public void setMember_id(int member_id) {
		this.member_id = member_id;
	}

	public int getMember_id() {
		return this.member_id;
	}

	public void setSay_id(int say_id) {
		this.say_id = say_id;
	}

	public int getSay_id() {
		return this.say_id;
	}

	public void setSaytime(String saytime) {
		this.saytime = saytime;
	}

	public String getSaytime() {
		return this.saytime;
	}

	public void setPraise_count(int praise_count) {
		this.praise_count = praise_count;
	}

	public int getPraise_count() {
		return this.praise_count;
	}

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}

	public String getNick_name() {
		return nick_name;
	}

	public void setNick_name(String nick_name) {
		this.nick_name = nick_name;
	}

	public int getIscollect() {
		return iscollect;
	}

	public void setIscollect(int iscollect) {
		this.iscollect = iscollect;
	}

	public int getIspraise() {
		return ispraise;
	}

	public void setIspraise(int ispraise) {
		this.ispraise = ispraise;
	}


	public Long getCollect_id() {
		return collect_id;
	}

	public void setCollect_id(Long collect_id) {
		this.collect_id = collect_id;
	}

	public class Say_content implements Serializable {
		private String content;

		private String oimg;

		private String timg;

		public void setContent(String content) {
			this.content = content;
		}

		public String getContent() {
			return this.content;
		}

		public void setOimg(String oimg) {
			this.oimg = oimg;
		}

		public String getOimg() {
			return this.oimg;
		}

		public void setTimg(String timg) {
			this.timg = timg;
		}

		public String getTimg() {
			return this.timg;
		}

	}
}
