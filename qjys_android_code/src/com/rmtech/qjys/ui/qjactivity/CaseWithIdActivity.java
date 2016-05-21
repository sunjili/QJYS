package com.rmtech.qjys.ui.qjactivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;

import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.FolderDataInfo;
import com.rmtech.qjys.model.gson.MImageList.ImageDataList;
import com.rmtech.qjys.ui.BaseActivity;

public class CaseWithIdActivity extends BaseActivity {
	protected String caseId;
	protected String folderId = "";
	protected CaseInfo caseInfo;
	protected ImageDataList imageDataList;
	protected FolderDataInfo folderDataInfo;

	public static void setCaseId(Intent intent, String patient_id) {
		intent.putExtra("patient_id", patient_id);
	}
	public static void setFolderId(Intent intent, String FolderId) {
		intent.putExtra("FolderId", FolderId);
	}

	public static void setCaseInfo(Intent intent, CaseInfo caseInfo) {
		intent.putExtra("case_info", (Parcelable) caseInfo);
	}
	public static void setFolderDataInfo(Intent intent, FolderDataInfo folderDataInfo) {
		if(folderDataInfo != null) {
			intent.putExtra("folderDataInfo", (Parcelable) folderDataInfo);
		}
	}

	public static void setImageDataList(Intent intent, ImageDataList imageDataList) {
		intent.putExtra("imageDataList", (Parcelable) imageDataList);
	}

	@Override
	protected boolean showTitleBar() {
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null) {
			caseId = intent.getStringExtra("patient_id");
			folderId = intent.getStringExtra("FolderId");
			caseInfo = (CaseInfo) intent.getParcelableExtra("case_info");
			folderDataInfo = (FolderDataInfo) intent.getParcelableExtra("folderDataInfo");
			imageDataList = (ImageDataList) intent.getParcelableExtra("imageDataList");

		}
	}

}
