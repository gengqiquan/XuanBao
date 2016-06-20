package com.aibaide.xuanbao.bean;

import java.io.Serializable;
import java.util.List;

public class ExerciseGoodsBean implements Serializable {
	private int goodsPoint;

	private int downLine;

	private int tstate;

	private String remark;
	private String activityUrl;
	private String urlName;

	private String goodsDetailsColor;

	private int state;

	private String goodsCode;

	private int goodsNum;

	private int activityId;

	private int winNumberCount;
	private int disTime;

	private String editInfo;
	private String filePath;

	private Long browseCount;
	private Long winNumber;
	private String activityEndTime;

	private int goodsDetailsId;

	private int goodsPrice;

	private String goodsDetailsSpec;

	private String goodsExplain;

	private String goodsName;

	private String activityStartTime;

	private int mstate;

	private List<sysFileUploadList> sysFileUploadList;

	private List<bissMembers> bissMembers;

	public void setGoodsPoint(int goodsPoint) {
		this.goodsPoint = goodsPoint;
	}

	public int getGoodsPoint() {
		return this.goodsPoint;
	}

	public void setDownLine(int downLine) {
		this.downLine = downLine;
	}

	public int getDownLine() {
		return this.downLine;
	}

	public void setTstate(int tstate) {
		this.tstate = tstate;
	}

	public int getTstate() {
		return this.tstate;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRemark() {
		return this.remark;
	}

	public void setGoodsDetailsColor(String goodsDetailsColor) {
		this.goodsDetailsColor = goodsDetailsColor;
	}

	public String getGoodsDetailsColor() {
		return this.goodsDetailsColor;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return this.state;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsCode() {
		return this.goodsCode;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public int getGoodsNum() {
		return this.goodsNum;
	}

	public void setActivityId(int activityId) {
		this.activityId = activityId;
	}

	public int getActivityId() {
		return this.activityId;
	}

	public void setWinNumberCount(int winNumberCount) {
		this.winNumberCount = winNumberCount;
	}

	public int getWinNumberCount() {
		return this.winNumberCount;
	}

	public void setEditInfo(String editInfo) {
		this.editInfo = editInfo;
	}

	public String getEditInfo() {
		return this.editInfo;
	}

	public void setBrowseCount(Long browseCount) {
		this.browseCount = browseCount;
	}

	public Long getBrowseCount() {
		return this.browseCount;
	}

	public void setActivityEndTime(String activityEndTime) {
		this.activityEndTime = activityEndTime;
	}

	public String getActivityEndTime() {
		return this.activityEndTime;
	}

	public void setGoodsDetailsId(int goodsDetailsId) {
		this.goodsDetailsId = goodsDetailsId;
	}

	public int getGoodsDetailsId() {
		return this.goodsDetailsId;
	}

	public void setGoodsPrice(int goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public int getGoodsPrice() {
		return this.goodsPrice;
	}

	public void setGoodsDetailsSpec(String goodsDetailsSpec) {
		this.goodsDetailsSpec = goodsDetailsSpec;
	}

	public String getGoodsDetailsSpec() {
		return this.goodsDetailsSpec;
	}

	public void setGoodsExplain(String goodsExplain) {
		this.goodsExplain = goodsExplain;
	}

	public String getGoodsExplain() {
		return this.goodsExplain;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsName() {
		return this.goodsName;
	}

	public void setActivityStartTime(String activityStartTime) {
		this.activityStartTime = activityStartTime;
	}

	public String getActivityStartTime() {
		return this.activityStartTime;
	}

	public void setMstate(int mstate) {
		this.mstate = mstate;
	}

	public int getMstate() {
		return this.mstate;
	}

	public void setSysFileUploadList(List<sysFileUploadList> sysFileUploadList) {
		this.sysFileUploadList = sysFileUploadList;
	}

	public List<sysFileUploadList> getSysFileUploadList() {
		return this.sysFileUploadList;
	}

	public void setBissMembers(List<bissMembers> bissMembers) {
		this.bissMembers = bissMembers;
	}

	public List<bissMembers> getBissMembers() {
		return this.bissMembers;
	}

	public Long getWinNumber() {
		return winNumber;
	}

	public void setWinNumber(Long winNumber) {
		this.winNumber = winNumber;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public int getDisTime() {
		return disTime;
	}

	public void setDisTime(int disTime) {
		this.disTime = disTime;
	}

	public String getActivityUrl() {
		return activityUrl;
	}

	public void setActivityUrl(String activityUrl) {
		this.activityUrl = activityUrl;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public class bissMembers implements Serializable{
		private String phone;

		private String nickName;

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getPhone() {
			return this.phone;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public String getNickName() {
			return this.nickName;
		}

	}

	public class sysFileUploadList implements Serializable{
		private int fileState;

		private String fileName;

		private int Id;

		private String fileUrl;

		public void setFileState(int fileState) {
			this.fileState = fileState;
		}

		public int getFileState() {
			return this.fileState;
		}

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
