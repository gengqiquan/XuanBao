package com.aibaide.xuanbao.bean;

public class ReportBean {
	private Long 上线ID;

	private Long 报告积分;
	private Long 商家id;

	private String 报告类型;

	private String 图片地址;
	private String 商品名称;
	private String 下单时间;

	public Long get上线ID() {
		return 上线ID;
	}

	public void set上线ID(Long 上线id) {
		上线ID = 上线id;
	}

	public Long get报告积分() {
		return 报告积分;
	}

	public void set报告积分(Long 报告积分) {
		this.报告积分 = 报告积分;
	}

	public String get报告类型() {
		return 报告类型;
	}

	public void set报告类型(String 报告类型) {
		this.报告类型 = 报告类型;
	}

	public String get图片地址() {
		return 图片地址;
	}

	public void set图片地址(String 图片地址) {
		this.图片地址 = 图片地址;
	}

	public String get商品名称() {
		return 商品名称;
	}

	public void set商品名称(String 商品名称) {
		this.商品名称 = 商品名称;
	}

	public String get下单时间() {
		return 下单时间;
	}

	public void set下单时间(String 下单时间) {
		this.下单时间 = 下单时间;
	}

	public Long get商家id() {
		return 商家id;
	}

	public void set商家id(Long 商家id) {
		this.商家id = 商家id;
	}

}
