package com.aibaide.xuanbao.bean;

import java.io.Serializable;
import java.util.List;

public class GoodsBean implements Serializable {

	public boolean chedked;
	private Long goodsSurplusNum;

	private int goodsPoint;

	private int tstate;

	private int downLine;

	private Long goodsLineNum;
	private Long collectId;

	private String filePath;
	private String lineUrl;
	private String urlName;

	private int disTime;
	private int state;

	private String goodsDetailsColor;

	private int cstate;

	private String goodsCode;

	private String lineEndTime;

	private String lineStartTime;

	private String editInfo;

	private Long browseCount;

	private Long goodsPrice;

	private String goodsDetailsSpec;

	private int isMark;

	private String goodsExplain;

	private String goodsName;

	private int lineId;

	private List<gdLineDetailsList> gdLineDetailsList;

	private List<sysFileUploadList> sysFileUploadList;

	public void setGoodsSurplusNum(Long goodsSurplusNum) {
		this.goodsSurplusNum = goodsSurplusNum;
	}

	public Long getGoodsSurplusNum() {
		return this.goodsSurplusNum;
	}

	public void setGoodsPoint(int goodsPoint) {
		this.goodsPoint = goodsPoint;
	}

	public int getGoodsPoint() {
		return this.goodsPoint;
	}

	public void setTstate(int tstate) {
		this.tstate = tstate;
	}

	public int getTstate() {
		return this.tstate;
	}

	public void setDownLine(int downLine) {
		this.downLine = downLine;
	}

	public int getDownLine() {
		return this.downLine;
	}

	public void setGoodsLineNum(Long goodsLineNum) {
		this.goodsLineNum = goodsLineNum;
	}

	public Long getGoodsLineNum() {
		return this.goodsLineNum;
	}


	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return this.state;
	}

	public void setGoodsDetailsColor(String goodsDetailsColor) {
		this.goodsDetailsColor = goodsDetailsColor;
	}

	public String getGoodsDetailsColor() {
		return this.goodsDetailsColor;
	}

	public void setCstate(int cstate) {
		this.cstate = cstate;
	}

	public int getCstate() {
		return this.cstate;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getGoodsCode() {
		return this.goodsCode;
	}

	public void setLineEndTime(String lineEndTime) {
		this.lineEndTime = lineEndTime;
	}

	public String getLineEndTime() {
		return this.lineEndTime;
	}

	public void setLineStartTime(String lineStartTime) {
		this.lineStartTime = lineStartTime;
	}

	public String getLineStartTime() {
		return this.lineStartTime;
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

	public void setGoodsPrice(Long goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	public Long getGoodsPrice() {
		return this.goodsPrice;
	}

	public void setGoodsDetailsSpec(String goodsDetailsSpec) {
		this.goodsDetailsSpec = goodsDetailsSpec;
	}

	public String getGoodsDetailsSpec() {
		return this.goodsDetailsSpec;
	}

	public void setIsMark(int isMark) {
		this.isMark = isMark;
	}

	public int getIsMark() {
		return this.isMark;
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

	public void setLineId(int lineId) {
		this.lineId = lineId;
	}

	public int getLineId() {
		return this.lineId;
	}

	public void setGdLineDetailsList(List<gdLineDetailsList> gdLineDetailsList) {
		this.gdLineDetailsList = gdLineDetailsList;
	}

	public List<gdLineDetailsList> getGdLineDetailsList() {
		return this.gdLineDetailsList;
	}

	public void setSysFileUploadList(List<sysFileUploadList> sysFileUploadList) {
		this.sysFileUploadList = sysFileUploadList;
	}

	public List<sysFileUploadList> getSysFileUploadList() {
		return this.sysFileUploadList;
	}

	public int getDisTime() {
		return disTime;
	}

	public void setDisTime(int disTime) {
		this.disTime = disTime;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getLineUrl() {
		return lineUrl;
	}

	public void setLineUrl(String lineUrl) {
		this.lineUrl = lineUrl;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}

	public Long getCollectId() {
		return collectId;
	}

	public void setCollectId(Long collectId) {
		this.collectId = collectId;
	}

	public class gdLineDetailsList implements Serializable{
		private Long goodsSurplusNum;

		private Long lineDetailsId;

		private Long isPay;

		private float payPrice;

		private String storeName;

		private Long goodsLineNum;

		private String addressIdName;

		private String addressDetail;

		private Long storeId;

		private Long isMail;

		public Long getGoodsSurplusNum() {
			return goodsSurplusNum;
		}

		public void setGoodsSurplusNum(Long goodsSurplusNum) {
			this.goodsSurplusNum = goodsSurplusNum;
		}

		public Long getLineDetailsId() {
			return lineDetailsId;
		}

		public void setLineDetailsId(Long lineDetailsId) {
			this.lineDetailsId = lineDetailsId;
		}

		public Long getIsPay() {
			return isPay;
		}

		public void setIsPay(Long isPay) {
			this.isPay = isPay;
		}

		public float getPayPrice() {
			return payPrice;
		}

		public void setPayPrice(float payPrice) {
			this.payPrice = payPrice;
		}

		public String getStoreName() {
			return storeName;
		}

		public void setStoreName(String storeName) {
			this.storeName = storeName;
		}

		public Long getGoodsLineNum() {
			return goodsLineNum;
		}

		public void setGoodsLineNum(Long goodsLineNum) {
			this.goodsLineNum = goodsLineNum;
		}

		public String getAddressIdName() {
			return addressIdName;
		}

		public void setAddressIdName(String addressIdName) {
			this.addressIdName = addressIdName;
		}

		public String getAddressDetail() {
			return addressDetail;
		}

		public void setAddressDetail(String addressDetail) {
			this.addressDetail = addressDetail;
		}

		public Long getStoreId() {
			return storeId;
		}

		public void setStoreId(Long storeId) {
			this.storeId = storeId;
		}

		public Long getIsMail() {
			return isMail;
		}

		public void setIsMail(Long isMail) {
			this.isMail = isMail;
		}


	}

	public class sysFileUploadList implements Serializable{
		private String fileName;

		private int fileState;
		private Long Id;

		private String fileUrl;

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}

		public String getFileName() {
			return this.fileName;
		}

		public void setId(Long Id) {
			this.Id = Id;
		}

		public Long getId() {
			return this.Id;
		}

		public void setFileUrl(String fileUrl) {
			this.fileUrl = fileUrl;
		}

		public String getFileUrl() {
			return this.fileUrl;
		}

		public int getFileState() {
			return fileState;
		}

		public void setFileState(int fileState) {
			this.fileState = fileState;
		}
	}

}
