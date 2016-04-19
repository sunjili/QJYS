package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;

import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.PhotoDataGridAdapter;
import com.rmtech.qjys.model.FolderDataInfo;
import com.rmtech.qjys.model.PhotoDataInfo;
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
	}

	public static void show(Activity context, String patient_id) {
		Intent intent = new Intent();
		setCaseId(intent, patient_id);
		intent.setClass(context, PhotoDataUploadActivity.class);
		context.startActivity(intent);
	}

	private void initViews() {
		initPhotoSelector();
		mContext = this;
		mGridView = (GridView) findViewById(R.id.dynamic_grid);
		nodata_layout = findViewById(R.id.nodata_layout);
		mAdapter = new PhotoDataGridAdapter(getActivity(), new ArrayList(),getResources().getInteger(R.integer.column_count));
		mGridView.setAdapter(mAdapter);

		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
				// selectImageFromGrid(i);
			}

		});

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
