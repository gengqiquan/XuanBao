package com.aibaide.xuanbao.bean;

import java.io.Serializable;

public class PictureBean implements Serializable {
	private int fileBelong;

	private String fileName;

	private String fileUrl;
	private Long forid;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Long getForid() {
		return forid;
	}

	public void setForid(Long forid) {
		this.forid = forid;
	}

	public int getFileBelong() {
		return fileBelong;
	}

	public void setFileBelong(int fileBelong) {
		this.fileBelong = fileBelong;
	}
}
