package com.aibaide.xuanbao.bean;

import java.io.Serializable;

/**
 * @author gengqiquan:
 * @version 创建时间：2016-4-25 上午10:28:39 类说明
 */
public class ReportEditBean implements Serializable{
	public ReportEditBean(int t,String i, String p) {
		type = t;
		text = "";
		img = i;
		filePath = p;
	}

	public String text;
	public String filePath;
	public String img;
	public int type;// 0-选择，1-文字，2-图片,-1-最后一个

}
