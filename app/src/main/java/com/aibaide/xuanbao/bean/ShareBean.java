package com.aibaide.xuanbao.bean;

import java.io.Serializable;

/**
 * @author gengqiquan:
 * @version 创建时间：2016-4-19 下午2:28:14
 * 类说明
 */
public class ShareBean  implements Serializable{
	private int shared;

	private int id;

	private int share_count;

	private int share_integral;

	private String share_title;

	private String file_url;

	public void setShared(int shared){
	this.shared = shared;
	}
	public int getShared(){
	return this.shared;
	}
	public void setId(int id){
	this.id = id;
	}
	public int getId(){
	return this.id;
	}
	public void setShare_count(int share_count){
	this.share_count = share_count;
	}
	public int getShare_count(){
	return this.share_count;
	}
	public void setShare_integral(int share_integral){
	this.share_integral = share_integral;
	}
	public int getShare_integral(){
	return this.share_integral;
	}
	public void setShare_title(String share_title){
	this.share_title = share_title;
	}
	public String getShare_title(){
	return this.share_title;
	}
	public void setFile_url(String file_url){
	this.file_url = file_url;
	}
	public String getFile_url(){
	return this.file_url;
	}
}
