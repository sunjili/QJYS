package com.rmtech.qjys.ui.qjactivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.model.FolderDataInfo;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.ChatActivity;
import com.rmtech.qjys.ui.qjactivity.PhotoDataBaseActivity.OnDeleteCallback;
import com.rmtech.qjys.utils.NewFolderManager;
import com.rmtech.qjys.utils.NewFolderManager.OnNewFolderListener;
import com.rmtech.qjys.utils.NewFolderManager.OnRenameListener;
import com.rmtech.qjys.utils.PhotoUploadManager;
import com.rmtech.qjys.utils.PhotoUploadManager.OnPhotoUploadListener;
import com.rmtech.qjys.utils.PhotoUploadStateInfo;
import com.rmtech.qjys.utils.PhotoUtil;
import com.sjl.lib.alertview.AlertView;
import com.sjl.lib.filechooser.FileUtils;
import com.sjl.lib.multi_image_selector.MultiImageSelectorActivity;
import com.sjl.lib.utils.L;

public class PhotoDataBaseActivity extends CaseWithIdActivity implements OnNewFolderListener, OnPhotoUploadListener {

	private NewFolderManager mNewFolderManager;

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		mNewFolderManager = new NewFolderManager(getActivity());
		//这个条件改了！！！!caseInfo.participate_doctor.isEmpty()暂时去掉
		if (caseInfo != null && caseInfo.participate_doctor != null) {
			addRightIcon(R.drawable.btn_intogroupchat, new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(getActivity(), ChatActivity.class);
					intent.putExtra(QjConstant.EXTRA_CHAT_TYPE, QjConstant.CHATTYPE_GROUP);
					intent.putExtra(QjConstant.EXTRA_USER_ID, caseInfo.group_id);
					startActivity(intent);
				}
			});
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		PhotoUploadManager.getInstance().registerPhotoUploadListener(this);

	}

	@Override
	protected void onDestroy() {

		PhotoUploadManager.getInstance().unregisterPhotoUploadListener(this);
		super.onDestroy();
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

	public void showNewFolderDialog(List<FolderDataInfo> folders) {
		if (mNewFolderManager != null) {
			String parentId = "";
			mNewFolderManager.showNewFolderDialog(caseId, parentId, folders, this);
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
	public void onAddNewFolder(FolderDataInfo info) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUploadProgress(PhotoUploadStateInfo state) {
		L.e("Upload progress" + state.progress);
	}

	@Override
	public void onUploadError(PhotoUploadStateInfo state, Exception e) {
		// TODO Auto-generated method stub
		L.e("Upload onUploadError" + e);
		Toast.makeText(getActivity(), "上传失败！" + e.getMessage(), Toast.LENGTH_SHORT).show();

	}

	@Override
	public void onUploadComplete(PhotoUploadStateInfo state, PhotoDataInfo info) {
		// TODO Auto-generated method stub
		Toast.makeText(getActivity(), "上传成功！", Toast.LENGTH_SHORT).show();
	}

	public static interface OnDeleteCallback {
		public void onDeleteSuccess(FolderDataInfo item);
	}

	public static void deleteItem(final Context context, final Object item, final OnDeleteCallback callback) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("确定删除？").setCancelable(false).setNegativeButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				if (item instanceof PhotoDataInfo) {
					QjHttp.deleteImages(((PhotoDataInfo) item).id, new BaseModelCallback() {

						@Override
						public void onResponseSucces(MBase response) {
							Toast.makeText(context, "删除成功！", 1).show();
							if (callback != null) {
								callback.onDeleteSuccess((FolderDataInfo) item);
							}
						}

						@Override
						public void onError(Call call, Exception e) {
							// TODO Auto-generated method stub
							Toast.makeText(context, "删除失败！", 1).show();
						}
					});

				} else if (item instanceof FolderDataInfo) {
					QjHttp.deleteFolder(((FolderDataInfo) item).id, new BaseModelCallback() {

						@Override
						public void onResponseSucces(MBase response) {
							Toast.makeText(context, "删除成功！", 1).show();
							if (callback != null) {
								callback.onDeleteSuccess((FolderDataInfo) item);
							}
						}

						@Override
						public void onError(Call call, Exception e) {
							// TODO Auto-generated method stub
							Toast.makeText(context, "删除失败！", 1).show();
						}
					});
				}

			}
		}).setPositiveButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		}).create().show();

	}

	public static void renameItem(final Context context, final FolderDataInfo item,List<FolderDataInfo> folders, final OnRenameListener callback) {
		new NewFolderManager(context).showRenameDialog(item, folders, callback);
	}
}
