package com.aibaide.xuanbao.bean;
/**
 * @author gengqiquan:
 * @version 创建时间：2016-4-18 上午10:38:54
 * 类说明
 */
public class TaskItemBean {
	private String module_image;

	private int module_integral;

	private int module_code;
	private String module_url;

	private String module_name;

	public void setModule_image(String module_image) {
		this.module_image = module_image;
	}

	public String getModule_image() {
		return this.module_image;
	}

	public void setModule_integral(int module_integral) {
		this.module_integral = module_integral;
	}

	public int getModule_integral() {
		return this.module_integral;
	}

	public void setModule_code(int module_code) {
		this.module_code = module_code;
	}

	public int getModule_code() {
		return this.module_code;
	}

	public void setModule_name(String module_name) {
		this.module_name = module_name;
	}

	public String getModule_name() {
		return this.module_name;
	}

	public String getModule_url() {
		return module_url;
	}

	public void setModule_url(String module_url) {
		this.module_url = module_url;
	}
}
