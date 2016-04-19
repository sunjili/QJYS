package com.rmtech.qjys.utils;

import java.util.ArrayList;

import okhttp3.Call;

import android.text.TextUtils;
import android.util.SparseArray;

import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.model.MUrlData;
import com.rmtech.qjys.model.PhotoDataInfo;

public class PhotoUploadManager {
	static private PhotoUploadManager mInstance = null;

	public ArrayList<OnPhotoUploadListener> mListenerList = new ArrayList<>();

	public SparseArray<PhotoUploadStateInfo> taskMap = new SparseArray<PhotoUploadStateInfo>();

	public void registerPhotoUploadListener(OnPhotoUploadListener listener) {
		mListenerList.add(listener);
	}

	public void unregisterPhotoUploadListener(OnPhotoUploadListener listener) {
		mListenerList.remove(listener);
	}

	public static synchronized PhotoUploadManager getInstance() {
		if (mInstance == null) {
			mInstance = new PhotoUploadManager();
		}
		return mInstance;
	}

	public static int createKey(String caseId, String folder_id,
			PhotoDataInfo info) {
		String path = "";
		if (info != null && !TextUtils.isEmpty(info.localPath)) {
			path = info.localPath;
		}
		return (caseId + folder_id + path).hashCode();

	}

	public void addUploadTask(String caseId, String folder_id,
			PhotoDataInfo info) {
		final int key = createKey(caseId, folder_id, info);
		if (taskMap.get(key) != null) {
			// 图片正在上传
		} else {
			final PhotoUploadStateInfo task = new PhotoUploadStateInfo(caseId,
					folder_id, info);//
			task.setCallback(new QjHttpCallback<MUrlData>() {

				@Override
				public MUrlData parseNetworkResponse(String str)
						throws Exception {
					return null;
				}

				@Override
				public void onResponseSucces(MUrlData response) {
					for (OnPhotoUploadListener listener : mListenerList) {
						listener.onUploadComplete(task, response.data.url);
					}
				}

				@Override
				public void onError(Call call, Exception e) {
					for (OnPhotoUploadListener listener : mListenerList) {
						listener.onUploadError(task, e);
					}
				}

				public void inProgress(float progress) {
					for (OnPhotoUploadListener listener : mListenerList) {
						listener.onUploadProgress(task);
					}
				}
			});
			taskMap.put(key, task);
			task.upload();//
		}

	}

	public interface OnPhotoUploadListener {

		public void onUploadProgress(PhotoUploadStateInfo state);

		public void onUploadError(PhotoUploadStateInfo state, Exception e);

		public void onUploadComplete(PhotoUploadStateInfo state, String url);
	}

}