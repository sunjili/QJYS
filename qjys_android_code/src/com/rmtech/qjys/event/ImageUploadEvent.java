package com.rmtech.qjys.event;

import com.rmtech.qjys.utils.PhotoUploadStateInfo;

public class ImageUploadEvent {

	public int uploadingNumber = 0;
	public PhotoUploadStateInfo stateInfo;
	public boolean isAdd;

	public ImageUploadEvent(PhotoUploadStateInfo info, int uploadingNumber, boolean isAdd) {
		this.uploadingNumber = uploadingNumber;
		this.stateInfo = info;
		this.isAdd = isAdd;
	}

}
