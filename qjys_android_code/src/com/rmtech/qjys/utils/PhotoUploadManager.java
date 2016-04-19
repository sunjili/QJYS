package com.rmtech.qjys.utils;

import java.util.ArrayList;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;
import android.app.usage.UsageEvents.Event;
import android.text.TextUtils;
import android.util.SparseArray;

import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.event.ImageUploadEvent;
import com.rmtech.qjys.model.MUploadImageInfo;
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

	public SparseArray<PhotoUploadStateInfo> getUploadTaskArray() {
		return taskMap;
	}

	public void addUploadTask(String caseId, String folder_id,
			PhotoDataInfo info) {
		final int key = createKey(caseId, folder_id, info);
		if (taskMap.get(key) != null) {
			// 图片正在上传
		} else {
			final PhotoUploadStateInfo task = new PhotoUploadStateInfo(caseId,
					folder_id, info);//
			task.setCallback(new QjHttpCallback<MUploadImageInfo>() {

				@Override
				public MUploadImageInfo parseNetworkResponse(String str)
						throws Exception {
					return null;
				}

				@Override
				public void onResponseSucces(MUploadImageInfo response) {
					for (OnPhotoUploadListener listener : mListenerList) {
						listener.onUploadComplete(task, response.data);
					}
					taskMap.remove(key);
					postUploadEvent();
				}

				@Override
				public void onError(Call call, Exception e) {
					for (OnPhotoUploadListener listener : mListenerList) {
						listener.onUploadError(task, e);
					}
					taskMap.remove(key);
					postUploadEvent();

				}

				public void inProgress(float progress) {
					for (OnPhotoUploadListener listener : mListenerList) {
						listener.onUploadProgress(task);
					}
				}
			});
			taskMap.put(key, task);
			task.upload();//
			postUploadEvent();
		}

	}

	private void postUploadEvent() {
		EventBus.getDefault().post(new ImageUploadEvent(taskMap.size()));
	}

	public interface OnPhotoUploadListener {

		public void onUploadProgress(PhotoUploadStateInfo state);

		public void onUploadError(PhotoUploadStateInfo state, Exception e);

		public void onUploadComplete(PhotoUploadStateInfo state,
				PhotoDataInfo info);
	}

}