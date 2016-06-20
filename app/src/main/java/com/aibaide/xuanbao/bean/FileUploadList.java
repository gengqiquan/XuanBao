package com.aibaide.xuanbao.bean;

import java.io.Serializable;

public class FileUploadList implements Serializable{
	private int id;

	private int fileSize;

	private int fileState;


	private int forid;

	private int fileBelong;

	private String fileName;

	private String fileUrl;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return this.id;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getFileSize() {
		return this.fileSize;
	}

	public void setFileState(int fileState) {
		this.fileState = fileState;
	}

	public int getFileState() {
		return this.fileState;
	}


	public void setForid(int forid) {
		this.forid = forid;
	}

	public int getForid() {
		return this.forid;
	}

	public void setFileBelong(int fileBelong) {
		this.fileBelong = fileBelong;
	}

	public int getFileBelong() {
		return this.fileBelong;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return this.fileName;
	}


	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileUrl() {
		return this.fileUrl;
	}
}
