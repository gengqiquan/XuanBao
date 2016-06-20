package com.aibaide.xuanbao.bean;

import java.io.Serializable;
import java.util.List;

public class VirtualBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5619416128988849074L;
	private int virtualExchangeState;

	private int downLine;
	private int disTime;
	private int virtualTakeRate;

	private int virtualTakeState;
	private int tstate;

	private String virtualName;
	private String filePath;
	private String virtualUrl;
	private String urlName;

	private int state;

	private int virtualExchangeRate;

	private String virtualEndTime;

	private int virtualExchangePoint;

	private int virtualSurplusNum;
	private String virtualExplain;

	private int hasNot;

	private int virtualTakePoint;
	private Long browseCount;

	private int virtualPrice;

	private int virtualNum;

	private int virtualId;

	private String virtualStartTime;
	private String editInfo;

	private List<bVirtualOpts> bVirtualOpts;

	private List<sysFileUploadList> sysFileUploadList;

	public void setVirtualExchangeState(int virtualExchangeState) {
		this.virtualExchangeState = virtualExchangeState;
	}

	public int getVirtualExchangeState() {
		return this.virtualExchangeState;
	}

	public void setDownLine(int downLine) {
		this.downLine = downLine;
	}

	public int getDownLine() {
		return this.downLine;
	}

	public void setVirtualTakeRate(int virtualTakeRate) {
		this.virtualTakeRate = virtualTakeRate;
	}

	public int getVirtualTakeRate() {
		return this.virtualTakeRate;
	}

	public void setVirtualTakeState(int virtualTakeState) {
		this.virtualTakeState = virtualTakeState;
	}

	public int getVirtualTakeState() {
		return this.virtualTakeState;
	}

	public void setVirtualName(String virtualName) {
		this.virtualName = virtualName;
	}

	public String getVirtualName() {
		return this.virtualName;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return this.state;
	}

	public void setVirtualExchangeRate(int virtualExchangeRate) {
		this.virtualExchangeRate = virtualExchangeRate;
	}

	public int getVirtualExchangeRate() {
		return this.virtualExchangeRate;
	}

	public void setVirtualEndTime(String virtualEndTime) {
		this.virtualEndTime = virtualEndTime;
	}

	public String getVirtualEndTime() {
		return this.virtualEndTime;
	}

	public void setVirtualExchangePoint(int virtualExchangePoint) {
		this.virtualExchangePoint = virtualExchangePoint;
	}

	public int getVirtualExchangePoint() {
		return this.virtualExchangePoint;
	}

	public void setVirtualSurplusNum(int virtualSurplusNum) {
		this.virtualSurplusNum = virtualSurplusNum;
	}

	public int getVirtualSurplusNum() {
		return this.virtualSurplusNum;
	}

	public void setVirtualExplain(String virtualExplain) {
		this.virtualExplain = virtualExplain;
	}

	public String getVirtualExplain() {
		return this.virtualExplain;
	}

	public void setHasNot(int hasNot) {
		this.hasNot = hasNot;
	}

	public int getHasNot() {
		return this.hasNot;
	}

	public void setVirtualTakePoint(int virtualTakePoint) {
		this.virtualTakePoint = virtualTakePoint;
	}

	public int getVirtualTakePoint() {
		return this.virtualTakePoint;
	}

	public void setVirtualPrice(int virtualPrice) {
		this.virtualPrice = virtualPrice;
	}

	public int getVirtualPrice() {
		return this.virtualPrice;
	}

	public void setVirtualNum(int virtualNum) {
		this.virtualNum = virtualNum;
	}

	public int getVirtualNum() {
		return this.virtualNum;
	}

	public void setVirtualId(int virtualId) {
		this.virtualId = virtualId;
	}

	public int getVirtualId() {
		return this.virtualId;
	}

	public void setVirtualStartTime(String virtualStartTime) {
		this.virtualStartTime = virtualStartTime;
	}

	public String getVirtualStartTime() {
		return this.virtualStartTime;
	}

	public void setBVirtualOpts(List<bVirtualOpts> bVirtualOpts) {
		this.bVirtualOpts = bVirtualOpts;
	}

	public List<bVirtualOpts> getBVirtualOpts() {
		return this.bVirtualOpts;
	}

	public void setSysFileUploadList(List<sysFileUploadList> sysFileUploadList) {
		this.sysFileUploadList = sysFileUploadList;
	}

	public List<sysFileUploadList> getSysFileUploadList() {
		return this.sysFileUploadList;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getBrowseCount() {
		return browseCount;
	}

	public void setBrowseCount(Long browseCount) {
		this.browseCount = browseCount;
	}

	public int getDisTime() {
		return disTime;
	}

	public void setDisTime(int disTime) {
		this.disTime = disTime;
	}

	public int getTstate() {
		return tstate;
	}

	public void setTstate(int tstate) {
		this.tstate = tstate;
	}

	public String getEditInfo() {
		return editInfo;
	}

	public void setEditInfo(String editInfo) {
		this.editInfo = editInfo;
	}

	public String getVirtualUrl() {
		return virtualUrl;
	}

	public void setVirtualUrl(String virtualUrl) {
		this.virtualUrl = virtualUrl;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public class bVirtualOpts {
		private int surplusNumCODE;

		private int numCode;
		private boolean checked;
		private int optId;
		private String optContent;

		private int virtualId;

		public void setSurplusNumCODE(int surplusNumCODE) {
			this.surplusNumCODE = surplusNumCODE;
		}

		public int getSurplusNumCODE() {
			return this.surplusNumCODE;
		}

		public void setNumCode(int numCode) {
			this.numCode = numCode;
		}

		public int getNumCode() {
			return this.numCode;
		}

		public void setOptId(int optId) {
			this.optId = optId;
		}

		public int getOptId() {
			return this.optId;
		}

		public void setOptContent(String optContent) {
			this.optContent = optContent;
		}

		public String getOptContent() {
			return this.optContent;
		}

		public void setVirtualId(int virtualId) {
			this.virtualId = virtualId;
		}

		public int getVirtualId() {
			return this.virtualId;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

	}

	public class sysFileUploadList {
		private String fileName;

		private int Id;

		private String fileUrl;

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFileName() {
			return this.fileName;
		}

		public void setId(int Id) {
			this.Id = Id;
		}

		public int getId() {
			return this.Id;
		}

		public void setFileUrl(String fileUrl) {
			this.fileUrl = fileUrl;
		}

		public String getFileUrl() {
			return this.fileUrl;
		}

	}

}
