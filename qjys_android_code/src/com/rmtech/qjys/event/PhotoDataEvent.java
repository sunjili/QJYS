package com.rmtech.qjys.event;

import com.rmtech.qjys.model.PhotoDataInfo;

public class PhotoDataEvent {

	public static final int TYPE_ADD = 1;
	public static final int TYPE_EDIT = 2;
	public static final int TYPE_DELETE = 3;
	public int type;
	public PhotoDataInfo dataInfo;

	public PhotoDataEvent(int type, PhotoDataInfo info) {
		this.type = type;
		this.dataInfo = info;
	}

}
