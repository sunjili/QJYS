package com.rmtech.qjys.utils;

import java.util.List;

import okhttp3.Call;
import okhttp3.Request;

import com.google.gson.Gson;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.model.gson.MUploadImageInfo;
import com.sjl.lib.http.okhttp.OkHttpUtils;

public class PhotoUploadStateInfo extends QjHttpCallback<MUploadImageInfo> {
	public String caseId;

	public String folder_id = "";
	public String localPath;
	public int progress;
	public PhotoDataInfo imageInfo;
	// private QjHttpCallback<MUploadImageInfo> callback;
	private QjHttpCallback<MUploadImageInfo> callbackForList;
	private QjHttpCallback<MUploadImageInfo> callback;

	private int key;

	public PhotoUploadStateInfo() {

	}

	public PhotoUploadStateInfo(String caseId, String folder_id,
			PhotoDataInfo info) {
		this.caseId = caseId;
		this.folder_id = folder_id;
		this.imageInfo = info;
		this.localPath = info.localPath;
		key = PhotoUploadManager.createKey(caseId, folder_id, localPath);

	}

	public int getKey() {
		return key;
	}

	public void setCallbackForList(
			QjHttpCallback<MUploadImageInfo> qjHttpCallback) {
		callbackForList = qjHttpCallback;
	}

	public void setCallback(QjHttpCallback<MUploadImageInfo> qjHttpCallback) {
		callback = qjHttpCallback;
	}

	public void upload() {
		QjHttp.uploadImage(key, caseId, folder_id, imageInfo.name, localPath,
				this);
	}

	@Override
	public void onError(Call call, Exception e) {
		// TODO Auto-generated method stub
		progress = 0;
		imageInfo.state = PhotoDataInfo.STATE_UPLOAD_FAILED;
		if (callbackForList != null) {
			callbackForList.onError(call, e);
		}
		if (callback != null) {
			callback.onError(call, e);
		}
		callbackForList = null;
		callback = null;
	}

	public void onBefore(Request request) {

		if (callbackForList != null) {
			callbackForList.onBefore(request);
		}
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

		if (callbackForList != null) {
			callbackForList.onAfter();
		}
		if (callback != null) {
			callback.onAfter();
		}

	}

	/**
	 * UI Thread
	 * 
	 * @param progress
	 */
	public void inProgress(float pro) {
		int newprogress = (int) (pro * 100f);
		if (newprogress == progress) {
			return;
		}
		imageInfo.state = PhotoDataInfo.STATE_UPLOADING;
		this.progress = (int) (pro * 100f);
		if (progress >= 100) {
			progress = 99;
		}

		if (callbackForList != null) {
			callbackForList.inProgress(this.progress);
		}
		if (callback != null) {
			callback.inProgress(this.progress);
		}
	}

	@Override
	public void onResponseSucces(MUploadImageInfo response) {
		if (imageInfo == null) {
			imageInfo = response.data;
		} else {
			imageInfo.buildFromOther(response.data);
		}
		imageInfo.state = PhotoDataInfo.STATE_NORMAL;

		if (callbackForList != null) {
			callbackForList.onResponseSucces(response);
		}
		if (callback != null) {
			callback.onResponseSucces(response);
		}
		callbackForList = null;
		callback = null;
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

	public void cancel() {
		OkHttpUtils.getInstance().cancelTag(key);
	}

	public void retry() {
		if (imageInfo.state == PhotoDataInfo.STATE_UPLOAD_FAILED) {
			imageInfo.state = PhotoDataInfo.STATE_NORMAL;
			progress = 0;
			upload();
		}
	}

	public PhotoDataInfo getPhotoInfo() {
		return imageInfo;
	}

}