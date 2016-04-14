package com.rmtech.qjys.ui.qjactivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListPopupWindow;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.fragment.PhotoManagerFragment;
import com.rmtech.qjys.ui.view.CaseTopBarView;
import com.rmtech.qjys.ui.view.PhotoManangerPopWindow;
import com.sjl.lib.alertview.AlertView;
import com.sjl.lib.filechooser.FileUtils;
import com.sjl.lib.multi_image_selector.MultiImageSelectorActivity;

public class PhotoDataManagerActivity extends BaseActivity {

	private PhotoManagerFragment mPhotoManagerFragment;
	private CaseTopBarView mCaseTopBarView;

	private ListPopupWindow mFolderPopupWindow;
	private View rightTitleView;

	private static final int REQUEST_IMAGE = 2;
	private static final int REQUEST_CAMERA = 100;
	private static final int REQUEST_CODE = 6384;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_photo_manager);
		setTitle("上传影像资料");
		findViewById(R.id.btn_camera).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				new AlertView(null, null, "取消", null, new String[] { "拍照",
						"从手机相册中选择", "从资源管理器中选择" },
						PhotoDataManagerActivity.this,
						AlertView.Style.ActionSheet,
						new com.sjl.lib.alertview.OnItemClickListener() {

							@Override
							public void onItemClick(Object o, int position) {
								switch (position) {
								case 0:
									showCameraAction();
									break;
								case 1:
									startImageSelector();
									// ImageSelectorMainActivity.show(PhotoDataManagerActivity.this);
									break;
								case 2:
									showChooser();
									break;
								}

							}
						}).show();
			}
		});

		mPhotoManagerFragment = new PhotoManagerFragment();
		getSupportFragmentManager().beginTransaction()
				.add(R.id.container, mPhotoManagerFragment).commit();
		mCaseTopBarView = (CaseTopBarView) findViewById(R.id.topbar_view);
		mPhotoManagerFragment.setQuickReturnView(mCaseTopBarView);
		rightTitleView = setRightTitle(R.drawable.btn_case_more,
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						showPopWindow(rightTitleView);
					}
				});
	}

	private void showChooser() {
		// Use the GET_CONTENT intent from the utility class
		Intent target = com.sjl.lib.filechooser.FileUtils
				.createGetContentIntent();
		// Create the chooser Intent
		Intent intent = Intent.createChooser(target, "chooser_title");
		try {
			startActivityForResult(intent, REQUEST_CODE);
		} catch (ActivityNotFoundException e) {
			// The reason for the existence of aFileChooser
		}
	}

	/**
	 * 选择相机
	 */
	private void showCameraAction() {
		// 跳转到系统照相机
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (cameraIntent.resolveActivity(getActivity().getPackageManager()) != null) {
			// 设置系统相机拍照后的输出路径
			// 创建临时文件
//			String name = new DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA)) + ".jpg";   
			try {
				mTmpFile = FileUtils.createTmpFile(getActivity());
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (mTmpFile != null && mTmpFile.exists()) {
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(mTmpFile));
				startActivityForResult(cameraIntent, REQUEST_CAMERA);
			} else {
				Toast.makeText(getActivity(), "存储空间不足", Toast.LENGTH_SHORT)
						.show();
			}
		} else {
			Toast.makeText(getActivity(), R.string.msg_no_camera,
					Toast.LENGTH_SHORT).show();
		}
	}

	protected BaseActivity getActivity() {
		return PhotoDataManagerActivity.this;
	}

	private void startImageSelector() {

		Intent intent = new Intent(PhotoDataManagerActivity.this,
				MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
		// 最大可选择图片数量
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 9);
		// 选择模式
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, true);
		// 默认选择
		// if(mSelectPath != null && mSelectPath.size()>0){
		// intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
		// mSelectPath);
		// }
		startActivityForResult(intent, REQUEST_IMAGE);
	}

	@Override
	public void onSaveInstanceState(Bundle outState,
			PersistableBundle outPersistentState) {
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataManagerActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		if (mPhotoManagerFragment != null
				&& mPhotoManagerFragment.onBackPressed()) {
			return;
		} else {
			super.onBackPressed();
		}
	}

	private void showPopWindow(View anchorView) {

		if (mFolderPopupWindow == null) {
			mFolderPopupWindow = PhotoManangerPopWindow.createPopupList(
					PhotoDataManagerActivity.this, anchorView,
					new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent,
								View view, int position, long id) {
							switch (position) {
							case 0:
								mPhotoManagerFragment.showNewFolderDialog();
								break;
							case 1:
								PhotoDataSortActivity
										.show(PhotoDataManagerActivity.this);
								break;
							case 2:
								PhotoDataSelectActivity
										.show(PhotoDataManagerActivity.this);
								break;
							case 3:
								PhotoDataSettingActivity
										.show(PhotoDataManagerActivity.this);
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

	private File mTmpFile;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_CODE:
			// If the file selection was successful
			if (resultCode == RESULT_OK) {
				if (data != null) {
					// Get the URI of the selected file
					final Uri uri = data.getData();
					Log.i("ssssssssss", "Uri = " + uri.toString());
					try {
						// Get the file path from the URI
						final String path = FileUtils.getPath(getActivity(),
								uri);
						Toast.makeText(getActivity(), "File Selected: " + path,
								Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						Log.e("FileSelectorTestActivity", "File select error",
								e);
					}
				}
			}
			break;

		case REQUEST_IMAGE:

			if (resultCode == RESULT_OK) {
				ArrayList<String> mSelectPath = data
						.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				// StringBuilder sb = new StringBuilder();
				// for(String p: mSelectPath){
				// sb.append(p);
				// sb.append("\n");
				// }
				// mResultText.setText(sb.toString());
			}
			break;

		case REQUEST_CAMERA:

			if (resultCode == Activity.RESULT_OK) {
				if (mTmpFile != null) {
					if (mTmpFile != null) {
						sendBroadcast(new Intent(
								Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
								Uri.fromFile(mTmpFile)));

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

}
