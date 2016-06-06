package com.rmtech.qjys.ui.qjactivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;

import uk.co.senab.photoview.PhotoView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
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
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.utils.PhotoUploadManager;

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
			String path = saveImage();
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
					pd.dismiss();
					finish();
				}

				@Override
				public void onError(Call call, Exception e) {
					Toast.makeText(getActivity(), "保存失败 "+e.toString(), 1)
					.show();
					pd.dismiss();

				}
			});
			// }

			// }.execute();
			break;
		}

	}

	public static Bitmap convertViewToBitmap(View view) {
//		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
//				MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//		view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
//		view.buildDrawingCache();
//		Bitmap bitmap = view.getDrawingCache();
		
		view.setDrawingCacheEnabled(true);  
        Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());  
        view.setDrawingCacheEnabled(false);  
		return bmp;
	}

	public static String getSDPath() {
		boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		if (hasSDCard) {
			return Environment.getExternalStorageDirectory().toString() + "/saving_picture";
		} else
			return "/data/data/com.rmtech.qjys/saving_picture";
	}

	public String saveImage() {
		Bitmap bitmap = convertViewToBitmap(photoView);
		String strPath = getSDPath();

		try {
			File destDir = new File(strPath);
			if (!destDir.exists()) {
				Log.d("MagicMirror", "Dir not exist create it " + strPath);
				destDir.mkdirs();
				Log.d("MagicMirror", "Make dir success: " + strPath);
			}

			String strFileName = "IMG_edit";
			File imageFile = new File(strPath + "/" + strFileName);
			imageFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(imageFile);
			bitmap.compress(CompressFormat.JPEG, 50, fos);
			fos.flush();
			fos.close();
			return imageFile.getAbsolutePath();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
