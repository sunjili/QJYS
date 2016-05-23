package com.rmtech.qjys.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import okhttp3.Call;

import org.greenrobot.eventbus.EventBus;

import android.util.Log;
import android.util.SparseArray;

import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.event.ImageUploadEvent;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.model.gson.MUploadImageInfo;

public class PhotoUploadManager {
	static private PhotoUploadManager mInstance = null;

	public ArrayList<OnPhotoUploadListener> mListenerList = new ArrayList<>();

	public SparseArray<PhotoUploadStateInfo> taskMap = new SparseArray<PhotoUploadStateInfo>();
	
	private static ConcurrentLinkedQueue<PhotoUploadStateInfo> queue = new ConcurrentLinkedQueue<PhotoUploadStateInfo>();
	private static List<PhotoUploadStateInfo> uploadingList = Collections.synchronizedList(new ArrayList<PhotoUploadStateInfo>());

	private static final int MAX_UPLOAD_COUNT = 1;
	
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

	public static int createKey(String caseId, String folder_id, String localPath) {
		return (caseId + folder_id + localPath).hashCode();

	}

	public SparseArray<PhotoUploadStateInfo> getUploadTaskArray() {
		return taskMap;
	}

	public void addUploadTask(String caseId, String folder_id, PhotoDataInfo info) {
		final int key = createKey(caseId, folder_id, info.localPath);
		if (taskMap.get(key) != null) {
			// 图片正在上传
		} else {
			final PhotoUploadStateInfo task = new PhotoUploadStateInfo(caseId, folder_id, info);//
			queue.offer(task);
			taskMap.put(key, task);
			task.setCallback(new QjHttpCallback<MUploadImageInfo>() {

				@Override
				public MUploadImageInfo parseNetworkResponse(String str) throws Exception {
					return null;
				}

				@Override
				public void onResponseSucces(MUploadImageInfo response) {
					
					onUploadSuccess(key, task);
					for (OnPhotoUploadListener listener : mListenerList) {
						listener.onUploadComplete(task, response.data);
					}
				}

				@Override
				public void onError(Call call, Exception e) {
					onUploadError(task);
					for (OnPhotoUploadListener listener : mListenerList) {
						listener.onUploadError(task, e);
					}
				}

				public void inProgress(float progress) {
//					Log.d("ssssssssss", "PhotoUploadManager progress =" + progress);
					for (OnPhotoUploadListener listener : mListenerList) {
						listener.onUploadProgress(task);
					}
				}
			});
			postUploadEvent();
//			task.upload(key);//
		}
		checkAndUpload();
	}
	
	private void onUploadSuccess(int key, PhotoUploadStateInfo task) {
		taskMap.remove(key);
		uploadingList.remove(task);
		postUploadEvent();
		checkAndUpload();
	}
	
	private void onUploadError(PhotoUploadStateInfo task) {
		uploadingList.remove(task);
		postUploadEvent();
		checkAndUpload();
	}
	
	private void checkAndUpload() {
		int count  = uploadingList.size();
		if(count < MAX_UPLOAD_COUNT) {
			for(int i = 0; i< (MAX_UPLOAD_COUNT - count);i++) {
				PhotoUploadStateInfo task = queue.poll();
				if(task != null) {
					uploadingList.add(task);
					int key = taskMap.indexOfValue(task);
					task.upload(key);
				}
			}
		}
	}

	private void postUploadEvent() {
		EventBus.getDefault().post(new ImageUploadEvent(taskMap.size()));
	}

	public interface OnPhotoUploadListener {

		public void onUploadProgress(PhotoUploadStateInfo state);

		public void onUploadError(PhotoUploadStateInfo state, Exception e);

		public void onUploadComplete(PhotoUploadStateInfo state, PhotoDataInfo info);
	}

}