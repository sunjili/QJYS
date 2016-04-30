package com.rmtech.qjys.event;


public class DoctorEvent {

	public static final int TYPE_ADD = 1;
	public static final int TYPE_EDIT = 2;
	public static final int TYPE_DELETE = 3;
	public int type;

	public DoctorEvent(int type) {
		this.type = type;
	}

}
