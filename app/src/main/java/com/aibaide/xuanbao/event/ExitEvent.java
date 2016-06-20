package com.aibaide.xuanbao.event;

public class ExitEvent {
	String Name = "";
	public static String ALL = "all";

	public ExitEvent(String ActivityName) {
		Name = ActivityName;
	}

	public boolean compareTo(String ActivityName) {
		return ActivityName.equals(Name);
	}
}
