package com.rmtech.qjys.event;

import java.util.ArrayList;

import com.rmtech.qjys.model.PhotoDataInfo;

public class PhotoDataEvent {

	public static final int TYPE_ADD = 1;
	public static final int TYPE_EDIT = 2;
	public static final int TYPE_DELETE = 3;
	public static final int TYPE_MOVE = 3;
	public int type;
	public PhotoDataInfo dataInfo;
	public String caseId;
	public String folderId;
	public ArrayList<PhotoDataInfo> imagelist;

	public PhotoDataEvent(int type, PhotoDataInfo info) {
		this.type = type;
		this.dataInfo = info;
	}

	public PhotoDataEvent(int type) {
		this.type = type;
	}

	public void setMovedImageList(String caseId, String folderId,
			ArrayList<PhotoDataInfo> imagelist) {
		this.caseId = caseId;
		this.folderId = folderId;
		this.imagelist = imagelist;
	}

}
