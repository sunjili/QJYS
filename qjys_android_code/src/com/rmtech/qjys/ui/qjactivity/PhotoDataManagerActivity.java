package com.rmtech.qjys.ui.qjactivity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListPopupWindow;

import com.rmtech.qjys.R;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.FolderDataInfo;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.fragment.PhotoManagerFragment;
import com.rmtech.qjys.ui.view.CaseTopBarView;
import com.rmtech.qjys.ui.view.PhotoManangerPopWindow;

public class PhotoDataManagerActivity extends PhotoDataBaseActivity {

	private PhotoManagerFragment mPhotoManagerFragment;
	private CaseTopBarView mCaseTopBarView;

	private ListPopupWindow mFolderPopupWindow;
	private View rightTitleView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_photo_manager);
		setTitle("上传影像资料");

		mPhotoManagerFragment = new PhotoManagerFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.container, mPhotoManagerFragment).commit();
		mCaseTopBarView = (CaseTopBarView) findViewById(R.id.topbar_view);
		mPhotoManagerFragment.setQuickReturnView(mCaseTopBarView);
		rightTitleView = setRightTitle(R.drawable.btn_case_more, new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopWindow(rightTitleView);
			}
		});
		initPhotoSelector();
		
		mCaseTopBarView.setCaseInfo(caseInfo);
		mPhotoManagerFragment.setIds(caseInfo,"");
	}

	protected BaseActivity getActivity() {
		return PhotoDataManagerActivity.this;
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
	}

	public static void show(Activity context, CaseInfo caseInfo) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataManagerActivity.class);
		setCaseInfo(intent, caseInfo);
		setCaseId(intent, caseInfo.id);
		
		context.startActivity(intent);
	}

	private void showPopWindow(View anchorView) {

		if (mFolderPopupWindow == null) {
			mFolderPopupWindow = PhotoManangerPopWindow.createPopupList(PhotoDataManagerActivity.this, anchorView,
					new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							switch (position) {
							case 0:
								showNewFolderDialog();
								break;
							case 1:
								PhotoDataSortActivity.show(PhotoDataManagerActivity.this);
								break;
							case 2:
								PhotoDataSelectActivity.show(PhotoDataManagerActivity.this);
								break;
							case 3:
								PhotoDataSettingActivity.show(PhotoDataManagerActivity.this);
								break;
							}
							mFolderPopupWindow.dismiss();

						}
					});
		}

		if (mFolderPopupWindow.isShowing()) {
			mFolderPopupWindow.dismiss();
		} else {
			mFolderPopupWindow.show();
		}
	}

	@Override
	public void onAddNewFolder(FolderDataInfo info) {
		super.onAddNewFolder(info);
		mPhotoManagerFragment.addFolderToGrid(info);
	}


	protected synchronized void onImagePicked(List<String> paths) {
		mPhotoManagerFragment.onImagePicked(paths);
	}

}
