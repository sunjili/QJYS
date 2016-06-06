package com.rmtech.qjys.event;

import com.rmtech.qjys.utils.PhotoUploadStateInfo;

public class ImageUploadEvent {

	public static final int TYPE_ADD = 7;
	public static final int TYPE_DELETE = 8;
	
	public int uploadingNumber = 0;
	public PhotoUploadStateInfo stateInfo;
	public String folderId;
	public int type = 0;

	public ImageUploadEvent(PhotoUploadStateInfo info, int uploadingNumber, int type) {
		this.uploadingNumber = uploadingNumber;
		this.stateInfo = info;
		this.type = type;
	}

	public ImageUploadEvent(String folderId, int typeDelete) {
		this.folderId = folderId;
		this.type = typeDelete;

	}

}
