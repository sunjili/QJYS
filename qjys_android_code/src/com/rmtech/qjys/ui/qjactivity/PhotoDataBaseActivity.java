package com.rmtech.qjys.ui.qjactivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.utils.NewFolderManager;
import com.rmtech.qjys.utils.NewFolderManager.OnNewFolderListener;
import com.rmtech.qjys.utils.PhotoUtil;
import com.sjl.lib.alertview.AlertView;
import com.sjl.lib.filechooser.FileUtils;
import com.sjl.lib.multi_image_selector.MultiImageSelectorActivity;
import com.sjl.lib.utils.L;

public class PhotoDataBaseActivity extends BaseActivity implements OnNewFolderListener {

	private NewFolderManager mNewFolderManager;

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		mNewFolderManager = new NewFolderManager(getActivity());

	}

	protected void initPhotoSelector() {
		View cameraView = findViewById(R.id.btn_camera);
		if (cameraView != null) {
			cameraView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					new AlertView(null, null, "取消", null, new String[] { "拍照", "从手机相册中选择", "从资源管理器中选择" },
							getActivity(), AlertView.Style.ActionSheet,
							new com.sjl.lib.alertview.OnItemClickListener() {

								@Override
								public void onItemClick(Object o, int position) {
									switch (position) {
									case 0:
										try {
											mTmpFile = FileUtils.createTmpFile(getActivity());
										} catch (IOException e) {
											e.printStackTrace();
										}
										PhotoUtil.showCameraAction(getActivity(), mTmpFile);
										break;
									case 1:
										PhotoUtil.startImageSelector(getActivity(), true);
										// ImageSelectorMainActivity.show(PhotoDataManagerActivity.this);
										break;
									case 2:
										PhotoUtil.showChooser(getActivity());
										break;
									}

								}
							}).show();
				}
			});
		}
	}

	protected BaseActivity getActivity() {
		return PhotoDataBaseActivity.this;
	}

	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
	}

	protected boolean showTitleBar() {
		return true;
	}

	//
	// @Override
	// public void onBackPressed() {
	// if (mPhotoManagerFragment != null &&
	// mPhotoManagerFragment.onBackPressed()) {
	// return;
	// } else {
	// super.onBackPressed();
	// }
	// }

	protected void onImagePicked(List<String> paths) {

	}

	private File mTmpFile;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case QjConstant.REQUEST_CODE:
			// If the file selection was successful
			if (resultCode == RESULT_OK) {
				if (data != null) {
					// Get the URI of the selected file
					final Uri uri = data.getData();
					try {
						// Get the file path from the URI
						final String path = FileUtils.getPath(getActivity(), uri);
						L.e("文件选择：path = " + path);
						ArrayList<String> list = new ArrayList<String>();
						list.add(path);
						onImagePicked(list);
						Toast.makeText(getActivity(), "File Selected: " + path, Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						Log.e("FileSelectorTestActivity", "File select error", e);
					}
				}
			}
			break;

		case QjConstant.REQUEST_IMAGE:

			if (resultCode == RESULT_OK) {
				ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				// StringBuilder sb = new StringBuilder();
				// for(String p: mSelectPath){
				// sb.append(p);
				// sb.append("\n");
				// }
				// mResultText.setText(sb.toString());
				onImagePicked(mSelectPath);
			}
			break;

		case QjConstant.REQUEST_CAMERA:

			if (resultCode == Activity.RESULT_OK) {
				if (mTmpFile != null) {
					if (mTmpFile != null) {
						sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mTmpFile)));
						ArrayList<String> list = new ArrayList<String>();
						list.add(mTmpFile.getAbsolutePath());
						L.e("相机选择：path = " + mTmpFile.getAbsolutePath());

						onImagePicked(list);

						// Intent data = new Intent();
						// resultList.add(imageFile.getAbsolutePath());
						// data.putStringArrayListExtra(EXTRA_RESULT,
						// resultList);
						// setResult(RESULT_OK, data);
						// finish();
						// mCallback.onCameraShot(mTmpFile);
					}
				} else {
					while (mTmpFile != null && mTmpFile.exists()) {
						boolean success = mTmpFile.delete();
						if (success) {
							mTmpFile = null;
						}
					}
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void showNewFolderDialog() {
		if (mNewFolderManager != null) {
			mNewFolderManager.showNewFolderDialog(this);
		}

	}

	@Override
	public void onBackPressed() {
		if (mNewFolderManager != null && mNewFolderManager.isAlertShowing()) {
			mNewFolderManager.dismissAlert();
			return;
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public void onAddNewFolder(String name) {
		// TODO Auto-generated method stub

	}

}
