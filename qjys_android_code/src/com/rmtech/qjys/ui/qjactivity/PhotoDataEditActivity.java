package com.rmtech.qjys.ui.qjactivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;

import org.greenrobot.eventbus.EventBus;

import uk.co.senab.photoview.PhotoView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.event.PhotoDataEvent;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.model.gson.MUploadImageInfo;
import com.rmtech.qjys.utils.PhotoUploadManager;
import com.sjl.lib.filechooser.FileUtils;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class PhotoDataEditActivity extends CaseWithIdActivity implements OnClickListener {

	protected static final String TAG = PhotoDataEditActivity.class.getSimpleName();
	private PhotoView photoView;
	private RelativeLayout titleLayout;
	private TextView returnTv;
	private TextView titleTv;
	private TextView editTv;
	private RelativeLayout bottomLayout;
	private TextView mirrorTv;
	private TextView rotateTv;
	private PhotoDataInfo photoData;
	DisplayImageOptions optionsThumb = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.default_error)
			.showImageOnFail(R.drawable.default_error).resetViewBeforeLoading(true).cacheOnDisk(true)
			.cacheInMemory(true).build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		setContentView(R.layout.activity_qj_photo_edit);

		photoView = (PhotoView) findViewById(R.id.photoView);
		titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
		returnTv = (TextView) findViewById(R.id.return_tv);
		returnTv.setOnClickListener(this);

		titleTv = (TextView) findViewById(R.id.title_tv);
		editTv = (TextView) findViewById(R.id.edit_tv);
		editTv.setOnClickListener(this);
		bottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
		mirrorTv = (TextView) findViewById(R.id.mirror_tv);
		mirrorTv.setOnClickListener(this);
		rotateTv = (TextView) findViewById(R.id.rotate_tv);
		rotateTv.setOnClickListener(this);
		photoData = getIntent().getParcelableExtra("photo_data");
		if (photoData == null) {
			finish();
			return;
		}
		ImageLoader.getInstance().displayImage(photoData.origin_url, photoView, optionsThumb);
	}

	public static void show(Activity context, PhotoDataInfo photoData, String caseId, String folderId) {

		Intent intent = new Intent();
		intent.setClass(context, PhotoDataEditActivity.class);
		intent.putExtra("photo_data", (Parcelable) photoData);
		setCaseId(intent, caseId);
		setFolderId(intent, folderId);
		context.startActivity(intent);
	}

	@Override
	protected boolean showTitleBar() {
		return false;
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.return_tv:
			finish();
			break;
		case R.id.mirror_tv:
			photoView.setMirror();
			break;
		case R.id.rotate_tv:
			photoView.setRotationBy(-90);
			break;
		case R.id.edit_tv:
			final ProgressDialog pd = new ProgressDialog(this);
			pd.setCanceledOnTouchOutside(false);
			pd.show();
			// new AsyncTask<Void, Void, String>() {
			//

			//
			// @Override
			// protected String doInBackground(Void... params) {
			// return saveImage();
			// }
			//
			// @Override
			// protected void onPostExecute(String path) {
			// super.onPostExecute(path);
			Bitmap bitmap = convertViewToBitmap(photoView);

			String path = FileUtils.saveImage(getActivity(), bitmap);
			if (TextUtils.isEmpty(path)) {
				pd.dismiss();
				return;
			}
			int tag = PhotoUploadManager.createKey(caseId, folderId, path);
			int index = path.lastIndexOf('/');
			String name = path.substring(index + 1, path.length());
			QjHttp.uploadImage(tag, caseId, folderId, photoData.id, name, path, new QjHttpCallback<MUploadImageInfo>() {

				@Override
				public MUploadImageInfo parseNetworkResponse(String str) throws Exception {
					return new Gson().fromJson(str, MUploadImageInfo.class);
				}

				@Override
				public void onResponseSucces(MUploadImageInfo response) {
					PhotoDataInfo data = response.data;
					PhotoDataEvent event = new PhotoDataEvent(PhotoDataEvent.TYPE_EDIT, data);
					event.setMovedImageList(caseId, folderId, null);
					EventBus.getDefault().post(event);
					try {
						pd.dismiss();
					} catch (Exception e) {
						e.printStackTrace();
					}
					finish();
				}

				@Override
				public void onError(Call call, Exception e) {
					Toast.makeText(getActivity(), "保存失败 "+e.toString(), 1)
					.show();
					try {
						pd.dismiss();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			});
			// }

			// }.execute();
			break;
		}

	}

	public static Bitmap convertViewToBitmap(PhotoView view) {
//		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
//				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//		view.buildDrawingCache();
//		Bitmap bitmap = view.getDrawingCache();
		
//		view.setDrawingCacheEnabled(true);  
        Bitmap bitmap = view.getVisibleRectangleBitmap(); 
//        Bitmap  bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), view.getDrawMatrix(), true);  
//        view.setDrawingCacheEnabled(false);  
		return bitmap;
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
