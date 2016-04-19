package com.rmtech.qjys.utils;

import okhttp3.Call;
import okhttp3.Request;

import com.google.gson.Gson;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.model.MUploadImageInfo;
import com.rmtech.qjys.model.PhotoDataInfo;

public class PhotoUploadStateInfo extends QjHttpCallback<MUploadImageInfo> {
	private String caseId;

	private String folder_id = "";
	private String localPath;
	public int progress;
	private PhotoDataInfo imageInfo;
	private QjHttpCallback<MUploadImageInfo> callback;

	public PhotoUploadStateInfo() {

	}

	public PhotoUploadStateInfo(String caseId, String folder_id,
			PhotoDataInfo info) {
		this.caseId = caseId;
		this.folder_id = folder_id;
		this.imageInfo = info;
		this.localPath = info.localPath;

	}

	public void setCallback(QjHttpCallback<MUploadImageInfo> qjHttpCallback) {
		callback = qjHttpCallback;
	}

	public void upload() {
		QjHttp.uploadImage(caseId, folder_id, imageInfo.name, localPath, this);
	}

	@Override
	public void onError(Call call, Exception e) {
		// TODO Auto-generated method stub
		imageInfo.state = PhotoDataInfo.STATE_UPLOAD_FAILED;
		if (callback != null) {
			callback.onError(call, e);
		}
	}

	public void onBefore(Request request) {
		if (callback != null) {
			callback.onBefore(request);
		}
	}

	/**
	 * UI Thread
	 * 
	 * @param
	 */
	public void onAfter() {
		if (callback != null) {
			callback.onAfter();
		}
	}

	/**
	 * UI Thread
	 * 
	 * @param progress
	 */
	public void inProgress(float progress) {
		imageInfo.state = PhotoDataInfo.STATE_UPLOADING;
		this.progress = (int) (progress *100f);
		if (callback != null) {
			callback.inProgress(progress);
		}
	}

	@Override
	public void onResponseSucces(MUploadImageInfo response) {
		if(imageInfo == null) {
			imageInfo = response.data;
		} else {
			imageInfo.buildFromOther(response.data);
		}
		imageInfo.state = PhotoDataInfo.STATE_NORMAL;
		if (callback != null) {
			callback.onResponseSucces(response);
		}
	}

	@Override
	public MUploadImageInfo parseNetworkResponse(String str) throws Exception {
		// TODO Auto-generated method stub
		return new Gson().fromJson(str, MUploadImageInfo.class);
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public String getFolder_id() {
		return folder_id;
	}

	public void setFolder_id(String folder_id) {
		this.folder_id = folder_id;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	public float getProgress() {
		return progress;
	}

}