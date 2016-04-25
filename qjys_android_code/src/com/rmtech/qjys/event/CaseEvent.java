package com.rmtech.qjys.event;

public class CaseEvent {

	public static final int TYPE_ADD = 1;
	public static final int TYPE_EDIT = 2;
	public static final int TYPE_DELETE = 3;
	public static final int TYPE_GROUP_CHANGED = 4;
	public int type;

	public CaseEvent(int type) {
		this.type = type;
	}

}
