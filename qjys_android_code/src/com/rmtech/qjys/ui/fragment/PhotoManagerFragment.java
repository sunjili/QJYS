package com.rmtech.qjys.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.PhotoDataGridAdapter;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.FolderDataInfo;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.model.gson.MImageList;
import com.rmtech.qjys.model.gson.MImageList.ImageDataList;
import com.rmtech.qjys.ui.qjactivity.PhotoDataBrowseActivity;
import com.rmtech.qjys.ui.qjactivity.PhotoDataManagerActivity;
import com.rmtech.qjys.ui.view.CaseTopBarView;
import com.rmtech.qjys.utils.PhotoUploadManager;
import com.sjl.lib.alertview.AlertView;
import com.sjl.lib.dynamicgrid.DynamicGridView;
import com.sjl.lib.utils.L;

public class PhotoManagerFragment extends QjBaseFragment {

	protected static final String TAG = PhotoManagerFragment.class.getSimpleName();
	private DynamicGridView mGridView;

	private CaseTopBarView mQuickReturnView;
	private int mCachedVerticalScrollRange;
	private int mQuickReturnHeight;

	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mState = STATE_ONSCREEN;
	private int mScrollY;
	private int mMinRawY = 0;
	private TranslateAnimation anim;

	private PhotoDataGridAdapter mAdapter;
	private View nodata_layout;
	private String folderId;
	private CaseInfo caseInfo;
	protected ImageDataList imageDataList;

	public ImageDataList getImageDataList() {
		return imageDataList;
	}

	public PhotoManagerFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.bfragment_photo_manager, container, false);
		return rootView;
	}

	@Override
	protected void initView() {
		mGridView = (DynamicGridView) getView().findViewById(R.id.dynamic_grid);
		nodata_layout = getView().findViewById(R.id.nodata_layout);

	}

	// public boolean onBackPressed() {
	//
	//
	//
	// return false;
	// }

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

	@Override
	protected void setUpView() {

		mGridView.setEditModeEnabled(false);
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
					PhotoDataBrowseActivity.show(getActivity(), imagePosition, list);
				} else if (itemInfo instanceof FolderDataInfo) {
					PhotoDataManagerActivity.show(getActivity(), caseInfo, (FolderDataInfo)itemInfo);
				}
			}
		});
		mGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getActivity(), " onItemLongClick", Toast.LENGTH_SHORT).show();
				new AlertView(null, null, "取消", null, new String[] { "删除", "重命名" }, getActivity(),
						AlertView.Style.ActionSheet, new com.sjl.lib.alertview.OnItemClickListener() {

							@Override
							public void onItemClick(Object o, int position) {
								switch (position) {
								case 0:
									// showCameraAction();
									break;
								case 1:
									// startImageSelector();
									// ImageSelectorMainActivity.show(PhotoDataManagerActivity.this);
									break;
								}

							}
						}).show();
				return true;
			}
		});

		mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				// Log.d("ssssssssss","onGlobalLayout");

				if (mAdapter == null || mAdapter.getCount() == 0) {
					return;
				}
				mQuickReturnHeight = mQuickReturnView.getHeight();
				mGridView.computeScrollY();
				mCachedVerticalScrollRange = mGridView.getListHeight();
			}
		});

		mGridView.setOnScrollListener(new OnScrollListener() {
			@SuppressLint("NewApi")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

				if (mAdapter == null || mAdapter.getCount() == 0) {
					return;
				}
				mScrollY = 0;
				int translationY = 0;

				if (mGridView.scrollYIsComputed()) {
					mScrollY = mGridView.getComputedScrollY();
				}

				int rawY = mGridView.getTop() - Math.min(mCachedVerticalScrollRange - mGridView.getHeight(), mScrollY);
				// Log.d("sssssssssss","mGridView.getTop() = "+mGridView.getTop());
				// Log.d("sssssssssss","mCachedVerticalScrollRange = "+mCachedVerticalScrollRange);
				// Log.d("sssssssssss"," mGridView.getHeight() = "+
				// mGridView.getHeight());
				// Log.d("sssssssssss"," mScrollY = "+ mScrollY);

				switch (mState) {
				case STATE_OFFSCREEN:
					if (rawY <= mMinRawY) {
						mMinRawY = rawY;
					} else {
						mState = STATE_RETURNING;
					}
					translationY = rawY;
					break;

				case STATE_ONSCREEN:
					if (rawY < -mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					translationY = rawY;
					break;

				case STATE_RETURNING:
					translationY = (rawY - mMinRawY) - mQuickReturnHeight;
					if (translationY > 0) {
						translationY = 0;
						mMinRawY = rawY - mQuickReturnHeight;
					}

					if (rawY > 0) {
						mState = STATE_ONSCREEN;
						translationY = rawY;
					}

					if (translationY < -mQuickReturnHeight) {
						mState = STATE_OFFSCREEN;
						mMinRawY = rawY;
					}
					break;
				}
				if (translationY > 0) {
					translationY = 0;
				}

				// Log.d("ssssssssss","translationY = "+ translationY);

				/** this can be used if the build is below honeycomb **/
				if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
					anim = new TranslateAnimation(0, 0, translationY, translationY);
					anim.setFillAfter(true);
					anim.setDuration(0);
					mQuickReturnView.startAnimation(anim);
				} else {
					mQuickReturnView.setTranslationY(translationY);
				}

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
		});
	}

	public void setQuickReturnView(CaseTopBarView mCaseTopBarView) {
		// TODO Auto-generated method stub
		mQuickReturnView = mCaseTopBarView;

	}

	public void addFolderToGrid(FolderDataInfo info) {
		mAdapter.add(0, info);
		mAdapter.notifyDataSetChanged();
	}

	public void setIds(CaseInfo caseInfo, FolderDataInfo folderDataInfo ) {
		this.caseInfo = caseInfo;
		this.folderId = folderDataInfo == null ? "" : folderDataInfo.id;
		QjHttp.getImageList(caseInfo.id, folderId , new QjHttpCallback<MImageList>() {

			@Override
			public void onError(Call call, Exception e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onResponseSucces(MImageList response) {
				if (getActivity() == null || getActivity().isFinishing()) {
					return;
				}
				ArrayList<FolderDataInfo> list = new ArrayList<FolderDataInfo>();
				imageDataList = response.data;
				if (response.data != null) {
					if (response.data.folders != null) {
						list.addAll(0, response.data.folders);
					}
					if (response.data.images != null) {
						list.addAll(response.data.images);
					}
				}
				// for(int i = 0; i<20;i++) {
				// list.add(new FolderDataInfo());
				// }
				mAdapter = new PhotoDataGridAdapter(getActivity(), list);
				mGridView.setAdapter(mAdapter);
				onDataChanged();
			}

			@Override
			public MImageList parseNetworkResponse(String str) throws Exception {
				return new Gson().fromJson(str, MImageList.class);
			}

		});

	}

	public void onImagePicked(List<String> paths) {
		// Log.d("ssssssssssssssss", "onImagePicked");
		for (String path : paths) {
			PhotoDataInfo info = new PhotoDataInfo();
			info.localPath = path;
			L.e("localPath = " + path);
			int index = path.lastIndexOf('/');
			info.name = path.substring(index + 1, path.length());
			L.e("info.name = " + info.name);
			info.state = PhotoDataInfo.STATE_UPLOADING;
			mAdapter.add(info);
			PhotoUploadManager.getInstance().addUploadTask(caseInfo.id, "", info);
		}
		onDataChanged();
	}

}