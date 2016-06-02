package com.rmtech.qjys.ui.qjactivity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;

import com.rmtech.qjys.R;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.FolderDataInfo;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.model.gson.MImageList.ImageDataList;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.fragment.PhotoManagerFragment;
import com.rmtech.qjys.ui.view.CaseTopBarView;
import com.rmtech.qjys.ui.view.PhotoManangerPopWindow;
import com.rmtech.qjys.ui.view.PhotoManangerPopWindow.ListPopupWindowAdapter;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.rmtech.qjys.utils.PhotoUploadStateInfo;
import com.rmtech.qjys.utils.QjUtil;


import com.rmtech.qjys.ui.view.PhotoManangerPopWindow;
import com.rmtech.qjys.ui.view.PhotoManangerPopWindow.ListPopupWindowAdapter;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.rmtech.qjys.utils.QjUtil;

public class PhotoDataManagerActivity extends PhotoDataBaseActivity {

	private PhotoManagerFragment mPhotoManagerFragment;
	private CaseTopBarView mCaseTopBarView;

	
	private View rightTitleView;
	private boolean isFirstCreate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if(isNewCase) {
			return;
		}
		setContentView(R.layout.activity_qj_photo_manager);
		String name = caseInfo.name;
//		if(QjUtil.getStringBytesLen(name)>12){
//			if(name.length()>12){
//				name = caseInfo.name.substring(0, 3)+"...";
//			}else {
//				name = 
//			}
//		}
		if (isRootFolder()) {
			TextView textView = setTitle(name);
			textView.setMaxEms(4);
			textView.setEllipsize(TextUtils.TruncateAt.END);
			textView.setSingleLine();
		} else {
			setTitle(folderDataInfo.name);
			setLeftTitle("根目录");
		}
        setLeftTitle("病例列表");
		mPhotoManagerFragment = new PhotoManagerFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.container, mPhotoManagerFragment).commit();
		mCaseTopBarView = (CaseTopBarView) findViewById(R.id.topbar_view);
		mPhotoManagerFragment.setQuickReturnView(mCaseTopBarView);
		setRightTitleForPopWindow();
		initPhotoSelector();

		mCaseTopBarView.setCaseInfo(caseInfo);
		mPhotoManagerFragment.setIds(caseInfo, folderDataInfo);
		isFirstCreate = true;
	}
	
	
	
	@Override
	protected void onResume() {
		if(!isFirstCreate) {
			if(caseInfo != null) {
				CaseInfo tempCase = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(caseInfo.id);
				if(tempCase != null) {
					caseInfo = tempCase;
					mCaseTopBarView.setCaseInfo(caseInfo);
//					mPhotoManagerFragment.setIds(caseInfo, folderDataInfo);
				}
			}
		}
		isFirstCreate = false;
		super.onResume();
	}

	@Override
	protected ImageDataList getImageDataList() {
		if(mPhotoManagerFragment != null) {
			return mPhotoManagerFragment.getImageDataList();
		} 
		return null;
	}

	protected void setRightTitleForPopWindow() {
		rightTitleView = setRightIcon(R.drawable.btn_case_more, new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopWindow(rightTitleView);
			}
		});
		
	}

	protected BaseActivity getActivity() {
		return PhotoDataManagerActivity.this;
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
	}

	public static void show(Activity context, CaseInfo caseInfo, FolderDataInfo itemInfo) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataManagerActivity.class);
		setCaseInfo(intent, caseInfo);
		if(caseInfo != null) {
			setCaseId(intent, caseInfo.id);
		}
		setFolderDataInfo(intent, itemInfo);
		if(itemInfo != null) {
			setFolderId(intent,itemInfo.id);
		}
		context.startActivity(intent);
	}
	
	public static void show(Activity context, String caseId, FolderDataInfo itemInfo) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataManagerActivity.class);
		setCaseId(intent, caseId);
		setFolderDataInfo(intent, itemInfo);
		if(itemInfo != null) {
			setFolderId(intent,itemInfo.id);
		}
		context.startActivity(intent);
	}
	@Override
	public void onUploadComplete(PhotoUploadStateInfo state, PhotoDataInfo info) {
		if(mPhotoManagerFragment != null) {
			mPhotoManagerFragment.onUploadComplete(state);
		}
	}

	@Override
	public void onAddNewFolder(FolderDataInfo info) {
		super.onAddNewFolder(info);
		
		if(mPhotoManagerFragment != null) {
			mPhotoManagerFragment.addFolderToGrid(info);
		}
	}

	protected synchronized void onImagePicked(List<String> paths) {
		super.onImagePicked(paths);
		if(mPhotoManagerFragment != null) {
			mPhotoManagerFragment.onImagePicked(paths);
		}
	}

}
