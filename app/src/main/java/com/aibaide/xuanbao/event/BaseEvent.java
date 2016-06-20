package com.aibaide.xuanbao.event;

public class BaseEvent {
	public String data;

	public BaseEvent(String d,String type) {
		data = d;
		this.type=type;
	}

	public String type = "";

	public boolean c(String v) {
		return type.equals(v);
	}
}
