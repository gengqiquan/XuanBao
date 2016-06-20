package com.aibaide.xuanbao.bean;

import java.util.List;

public class LogisticsBean {
	private String ShipperCode;

	private String LogisticCode;

	private List<Traces> Traces;

	public void setShipperCode(String ShipperCode) {
		this.ShipperCode = ShipperCode;
	}

	public String getShipperCode() {
		return this.ShipperCode;
	}

	public void setLogisticCode(String LogisticCode) {
		this.LogisticCode = LogisticCode;
	}

	public String getLogisticCode() {
		return this.LogisticCode;
	}

	public void setTraces(List<Traces> Traces) {
		this.Traces = Traces;
	}

	public List<Traces> getTraces() {
		return this.Traces;
	}

	public class Traces {
		private String AcceptTime;

		private String AcceptStation;
		private String Remark;

		public void setAcceptTime(String AcceptTime) {
			this.AcceptTime = AcceptTime;
		}

		public String getAcceptTime() {
			return this.AcceptTime;
		}

		public void setAcceptStation(String AcceptStation) {
			this.AcceptStation = AcceptStation;
		}

		public String getAcceptStation() {
			return this.AcceptStation;
		}

		public String getRemark() {
			return Remark;
		}

		public void setRemark(String remark) {
			Remark = remark;
		}
	}
}
