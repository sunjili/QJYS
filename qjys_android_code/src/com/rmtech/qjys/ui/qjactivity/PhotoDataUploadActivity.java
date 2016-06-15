package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.PhotoDataGridAdapter;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.event.ImageUploadEvent;
import com.rmtech.qjys.event.PhotoDataEvent;
import com.rmtech.qjys.model.FolderDataInfo;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.gson.MImageList;
import com.rmtech.qjys.model.gson.MImageList.ImageDataList;
import com.rmtech.qjys.utils.NewFolderManager.OnRenameListener;
import com.rmtech.qjys.utils.PhotoUploadManager;
import com.rmtech.qjys.utils.PhotoUploadStateInfo;
import com.sjl.lib.alertview.AlertView;
import com.sjl.lib.db.DBUtil;
import com.sjl.lib.utils.L;
import com.umeng.analytics.MobclickAgent;

public class PhotoDataUploadActivity extends PhotoDataManagerActivity {

	protected Context mContext;
	protected GridView mGridView;
	protected View nodata_layout;

	private PhotoDataGridAdapter mAdapter;
	private View mRightView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		isNewCase = true;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_photo_upload);
		setTitle("上传影像资料");
		mRightView = setRightTitle(R.drawable.btn_case_newfolder, new OnClickListener() {

			@Override
			public void onClick(View v) {
				List<FolderDataInfo> folders = null;
				if (imageDataList != null) {
					folders = imageDataList.folders;
				}
				showNewFolderDialog(folders);
			}
		});
		initViews();
		try {
			if(!EventBus.getDefault().isRegistered(this)) {
				EventBus.getDefault().register(this);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void show(Activity context, String patient_id) {
		Intent intent = new Intent();
		setCaseId(intent, patient_id);
		intent.setClass(context, PhotoDataUploadActivity.class);
		context.startActivity(intent);
	}
	

	public static void show(Activity context, String caseId, FolderDataInfo itemInfo) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataUploadActivity.class);
		setCaseId(intent, caseId);
		setFolderDataInfo(intent, itemInfo);
		if(itemInfo != null) {
			setFolderId(intent,itemInfo.id);
		}
		context.startActivity(intent);
	}
	
	@Subscribe
	public void onEvent(ImageUploadEvent event) {
		if (getActivity() == null) {
			return;
		}
		if(event == null) {//
			return;
		}
		if (event.type == ImageUploadEvent.TYPE_DELETE && mAdapter != null
				&& mAdapter.getItems() != null) {
			for (Object obj : mAdapter.getItems()) {
				if ((obj instanceof FolderDataInfo)  && !(obj instanceof PhotoDataInfo)) {
					FolderDataInfo info = (FolderDataInfo) obj;
					if (TextUtils.equals(info.id, event.folderId)) {
						info.image_count -= 1;
						mAdapter.notifyDataSetChanged();
						break;
					}
				}
				
			}
		}
		if(event.stateInfo == null) {
			return;
		}
		if(!TextUtils.equals(event.stateInfo.getCaseId(), caseId)) {
			return;
		}
		if (event.type == ImageUploadEvent.TYPE_ADD && mAdapter != null
				&& mAdapter.getItems() != null) {
			for (Object obj : mAdapter.getItems()) {
				if ((obj instanceof FolderDataInfo)  && !(obj instanceof PhotoDataInfo)) {
					FolderDataInfo info = (FolderDataInfo) obj;
					if (TextUtils.equals(info.id, event.stateInfo.getFolder_id())) {
						info.image_count += 1;
						mAdapter.notifyDataSetChanged();
						break;
					}
				}

			}
		}

	}

	
	@Subscribe
	public void onEvent(PhotoDataEvent event) {
		if (getActivity() == null) {
			return;
		}
		if(event == null || !TextUtils.equals(event.caseId, caseId)) {
			return;
		}
		if (event.dataInfo != null && event.type == PhotoDataEvent.TYPE_EDIT && mAdapter != null
				&& mAdapter.getItems() != null) {
			for (Object obj : mAdapter.getItems()) {
				if (obj instanceof PhotoDataInfo) {
					PhotoDataInfo info = (PhotoDataInfo) obj;
					if (TextUtils.equals(info.id, event.dataInfo.id)) {
						info.origin_url = event.dataInfo.origin_url;
						info.thumb_url = event.dataInfo.thumb_url;
						info.localPath = null;
						if (mAdapter != null) {
							mAdapter.notifyDataSetChanged();
						}
						break;
					}
				}
			}
		}
		if (event.type == PhotoDataEvent.TYPE_ADD && mAdapter != null
				&& mAdapter.getItems() != null) {
			for (Object obj : mAdapter.getItems()) {
				if ((obj instanceof FolderDataInfo)  && !(obj instanceof PhotoDataInfo)) {
					FolderDataInfo info = (FolderDataInfo) obj;
					if (TextUtils.equals(info.id, event.folderId)) {
						info.image_count += 1;
						mAdapter.notifyDataSetChanged();
						break;
					}
				}
			}
		}
		if (event.type == PhotoDataEvent.TYPE_MOVE || event.type == PhotoDataEvent.TYPE_DELETE || event.type == PhotoDataEvent.TYPE_SORT) {
			if (!TextUtils.equals(caseId, event.caseId)) {
				return;
			}
			// if (TextUtils.equals(folderId, event.folderId)) {
			loadData();
			// return;
			// }

		}
	}

	private void loadData() {
		if (!TextUtils.isEmpty(caseId)) {
			QjHttp.getImageList(true, caseId, folderId, new QjHttpCallbackNoParse<MImageList>() {

				@Override
				public void onError(Call call, Exception e) {

				}

				@Override
				public void onResponseSucces(boolean iscache, MImageList response) {
					if (getActivity() == null || getActivity().isFinishing()) {
						return;
					}
					if (response.data != null) {
						mAdapter.clear();
						if (response.data.folders != null) {
							mAdapter.add(0, response.data.folders);
						}
						if (response.data.images != null) {
							mAdapter.add(response.data.images);
						}
					}
					onDataChanged();
				}
			});
		}
	}

	@Override
	public void onDestroy() {
		EventBus.getDefault().unregister(this);
		String cacheKey = QjHttp.URL_PATIENT_IMAGE_LIST + UserContext.getInstance().getCookie() + caseId + folderId;
		Object obj = DBUtil.getCache(cacheKey);
		if(obj != null && obj instanceof MImageList) {
			MImageList imagelist = (MImageList) obj;
			imagelist.data = imageDataList;
			DBUtil.saveCache(cacheKey, imagelist);
		}
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
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Object itemInfo = mAdapter.getItem(position);
				if (itemInfo instanceof PhotoDataInfo) {
					ArrayList<PhotoDataInfo> list = new ArrayList<PhotoDataInfo>();
					int floderNum = 0;
					for (Object obj : mAdapter.getItems()) {
						if (obj instanceof PhotoDataInfo) {
							list.add((PhotoDataInfo) obj);
						} else {
							floderNum++;
						}
					}
					int imagePosition = position - floderNum;
					if (imagePosition < 0 || imagePosition >= list.size()) {
						imagePosition = 0;
					}
					PhotoDataBrowseActivity.show(getActivity(), imagePosition, list, caseId, caseInfo, folderId);
				} else if (itemInfo instanceof FolderDataInfo) {
					PhotoDataUploadActivity.show(getActivity(), caseId, (FolderDataInfo) itemInfo);
				}
			}
		});
		mGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			private AlertView alertView;

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, final View view, final int itemposition, long id) {
				// Toast.makeText(getActivity(), " onItemLongClick",
				// Toast.LENGTH_SHORT).show();
				alertView = new AlertView(null, null, "取消", null, new String[] { "删除", "重命名" }, getActivity(),
						AlertView.Style.ActionSheet, new com.sjl.lib.alertview.OnItemClickListener() {

							@Override
							public void onItemClick(Object o, int position) {
								if (alertView != null) {
									alertView.forceDismiss();
								}
								switch (position) {
								case 0:
									PhotoDataBaseActivity.deleteItem(getActivity(),folderId, mAdapter.getItem(itemposition),
											new OnDeleteCallback() {

												@Override
												public void onDeleteSuccess(FolderDataInfo item) {
													mAdapter.remove(item);
												}

											});
									break;
								case 1:
									PhotoDataBaseActivity.renameItem(getActivity(),
											(FolderDataInfo) mAdapter.getItem(itemposition), imageDataList.folders,
											new OnRenameListener() {

												@Override
												public void onRenameSuccess(FolderDataInfo data) {
													TextView tv = (TextView) view.findViewById(R.id.item_name);
													if (tv != null) {
														tv.setText(data.name);
													}
												}

											});
									break;
								}

							}

						});
				alertView.show();

				return true;
			}
		});

		loadData();

	}

	@Override
	public void onAddNewFolder(FolderDataInfo info) {
		if (imageDataList == null) {
			imageDataList = new ImageDataList();
		}
		if (imageDataList.folders == null) {
			imageDataList.folders = new ArrayList<FolderDataInfo>();
		}

		imageDataList.folders.add(0, info);

		mAdapter.add(0, info);
		onDataChanged();
	}

	private void onDataChanged() {
		if (mAdapter.getCount() > 0) {
			if (nodata_layout.getVisibility() == View.VISIBLE) {
				nodata_layout.setVisibility(View.GONE);
			}
//			if(mRightView != null && mRightView.getVisibility() == View.VISIBLE) {
//				mRightView.setVisibility(View.GONE);
//			}
//			setRightTitleForPopWindow();
		} else {
			nodata_layout.setVisibility(View.VISIBLE);
		}
		mAdapter.notifyDataSetChanged();

	}

	protected synchronized void onImagePicked(List<String> paths) {
		super.onImagePicked(paths);
		if (imageDataList == null) {
			imageDataList = new ImageDataList();
		}
		if (imageDataList.images == null) {
			imageDataList.images = new ArrayList<PhotoDataInfo>();
		}
		super.onImagePicked(paths);
		for (String path : paths) {
			PhotoDataInfo info = new PhotoDataInfo();
			info.localPath = path;
			L.e("localPath = " + path);
			int index = path.lastIndexOf('/');
			info.name = path.substring(index + 1, path.length());
			L.e("info.name = " + info.name);
			info.state = PhotoDataInfo.STATE_UPLOADING;
//			mAdapter.add(info);
			PhotoUploadManager.getInstance().addUploadTask(caseId, folderId, info);
//			imageDataList.images.add(info);
		}
//		onDataChanged();
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
		if(TextUtils.equals(state.getCaseId(),caseId) && TextUtils.equals(state.getFolder_id(),folderId)) {
			imageDataList.images.add(state.getPhotoInfo());
			mAdapter.add(state.getPhotoInfo());
			onDataChanged();
//			
//			PhotoDataEvent event = new PhotoDataEvent(
//					PhotoDataEvent.TYPE_ADD);
//			event.setMovedImageList(caseId, folderId, null);
//			EventBus.getDefault().post(event);
			
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
