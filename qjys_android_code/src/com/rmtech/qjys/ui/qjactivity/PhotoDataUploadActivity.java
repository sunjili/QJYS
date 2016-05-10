package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import okhttp3.Call;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.PhotoDataGridAdapter;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.event.PhotoDataEvent;
import com.rmtech.qjys.model.FolderDataInfo;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.model.gson.MImageList;
import com.rmtech.qjys.utils.PhotoUploadManager;
import com.rmtech.qjys.utils.PhotoUploadStateInfo;
import com.sjl.lib.utils.L;

public class PhotoDataUploadActivity extends PhotoDataBaseActivity {

	protected Context mContext;
	protected GridView mGridView;
	protected View nodata_layout;

	private PhotoDataGridAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_photo_upload);
		setTitle("上传影像资料");
		setRightTitle(R.drawable.btn_case_newfolder, new OnClickListener() {

			@Override
			public void onClick(View v) {
				showNewFolderDialog();
			}
		});
		initViews();
		EventBus.getDefault().register(this);

	}

	public static void show(Activity context, String patient_id) {
		Intent intent = new Intent();
		setCaseId(intent, patient_id);
		intent.setClass(context, PhotoDataUploadActivity.class);
		context.startActivity(intent);
	}
	
	@Subscribe
	public void onEvent(PhotoDataEvent event) {
		if (event != null && event.dataInfo != null
				&& event.type == PhotoDataEvent.TYPE_EDIT && mAdapter != null && mAdapter.getItems() != null) {
			for (Object obj : mAdapter.getItems()) {
				if(obj instanceof PhotoDataInfo) {
					PhotoDataInfo info = (PhotoDataInfo)obj;
					if (TextUtils.equals(info .id, event.dataInfo.id)) {
						info.origin_url = event.dataInfo.origin_url;
						info.thumb_url = event.dataInfo.thumb_url;
						if (mAdapter != null) {
							mAdapter.notifyDataSetChanged();
						}
						break;
					}
				}
			}
		}
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		super.onDestroy();
	}

	private void initViews() {
		initPhotoSelector();
		mContext = this;
		mGridView = (GridView) findViewById(R.id.dynamic_grid);
		nodata_layout = findViewById(R.id.nodata_layout);
		mAdapter = new PhotoDataGridAdapter(getActivity(), new ArrayList());
		mGridView.setAdapter(mAdapter);

		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int i, long l) {
				// selectImageFromGrid(i);
			}

		});

		if (!TextUtils.isEmpty(caseId)) {
			QjHttp.getImageList(true, caseId, "",
					new QjHttpCallbackNoParse<MImageList>() {

						@Override
						public void onError(Call call, Exception e) {

						}

						@Override
						public void onResponseSucces(boolean iscache,
								MImageList response) {
							if (getActivity() == null
									|| getActivity().isFinishing()) {
								return;
							}
							// ArrayList<FolderDataInfo> list = new
							// ArrayList<FolderDataInfo>();
							// imageDataList = response.data;
							if (response.data != null) {
								mAdapter.clear();
								if (response.data.folders != null) {
									mAdapter.add(0, response.data.folders);
								}
								if (response.data.images != null) {
									mAdapter.add(response.data.images);
								}
							}
							// for(int i = 0; i<20;i++) {
							// list.add(new FolderDataInfo());
							// }
							// mAdapter = new
							// PhotoDataGridAdapter(getActivity(), list);
							// mGridView.setAdapter(mAdapter);
							onDataChanged();
						}

						// @Override
						// public MImageList parseNetworkResponse(String str)
						// throws Exception {
						// return new Gson().fromJson(str, MImageList.class);

					});

		}

	}

	@Override
	public void onAddNewFolder(FolderDataInfo info) {
		mAdapter.add(0, info);
		onDataChanged();
	}

	private void onDataChanged() {
		if (mAdapter.getCount() > 0) {
			if (nodata_layout.getVisibility() == View.VISIBLE) {
				nodata_layout.setVisibility(View.GONE);
			}
		} else {
			nodata_layout.setVisibility(View.VISIBLE);
		}
		mAdapter.notifyDataSetChanged();

	}

	protected synchronized void onImagePicked(List<String> paths) {
		Log.d("ssssssssssssssss", "onImagePicked");
		super.onImagePicked(paths);
		for (String path : paths) {
			PhotoDataInfo info = new PhotoDataInfo();
			info.localPath = path;
			L.e("localPath = " + path);
			int index = path.lastIndexOf('/');
			info.name = path.substring(index + 1, path.length());
			L.e("info.name = " + info.name);
			info.state = PhotoDataInfo.STATE_UPLOADING;
			mAdapter.add(info);
			PhotoUploadManager.getInstance().addUploadTask(caseId, "", info);
		}
		onDataChanged();
	}

	@Override
	public void onUploadProgress(PhotoUploadStateInfo state) {
		super.onUploadProgress(state);
		L.e("Upload progress" + state.progress);
	}

	@Override
	public void onUploadError(PhotoUploadStateInfo state, Exception e) {
		super.onUploadError(state, e);
		// TODO Auto-generated method stub
		L.e("Upload onUploadError" + e);

	}

	@Override
	public void onUploadComplete(PhotoUploadStateInfo state, PhotoDataInfo info) {
		super.onUploadComplete(state, info);
		L.e("Upload onUploadComplete =" + info.origin_url);

	}

}
