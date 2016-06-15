package com.rmtech.qjys.ui.qjactivity;

import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.view.CropImageView;
import com.rmtech.qjys.utils.BitmapUtils;
import com.rmtech.qjys.utils.ExifUtils;
import com.rmtech.qjys.utils.PhotoUtil;
import com.sjl.lib.utils.ScreenUtil;
import com.umeng.analytics.MobclickAgent;


public class ImageCropActivity extends BaseActivity {

	public final static int TYPE_USER_HEAD = 1;
	public final static int TYPE_USER_COVER = 2;
	
	public static final int minHeight = 120;
	public static final int minWidth = 120;

	private CropImageView mCropImageView;

	// private int mRawHeight;
	// private int mRawWidth;
	private int mOutputX = 680;
	private int mOutputY = 680;
	private int mType;
	private String mOutputPath;
	private Bitmap.CompressFormat mOutputFormat = Bitmap.CompressFormat.JPEG;

	private boolean mImageLoaded = false;
	
	public static void startActivity(Context ctx, int destWidth, int destHeight, Uri srcUri, Uri destUri){
		Intent intent = new Intent(ctx, ImageCropActivity.class);
		intent.setDataAndType(srcUri, "image/*");
		intent.putExtra("outputX", destWidth);// 输出图片大小
		intent.putExtra("outputY", destHeight);
		intent.putExtra("type", ImageCropActivity.TYPE_USER_HEAD);
		intent.putExtra("output", destUri.toString());

		try {
			Activity a = (Activity) ctx;
			a.startActivityForResult(intent,
					PhotoUtil.PHOTO_REQUEST_CUT_HEADER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_image_crop);
		findViewById(R.id.finish).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				if (mImageLoaded) {
					new CropHeadWorker().execute();
				}
			}
		});
		

		mCropImageView = (CropImageView) findViewById(R.id.src_pic);

		Intent intent = getIntent();

		if (intent == null) {
//			ToastUtil.showToast(this, "参数错误");
			finish();

			return;
		}

		Bundle extra = intent.getExtras();
		if (extra == null) {
//			ToastUtil.showToast(this, "参数错误");
			finish();

			return;
		}

		mOutputX = extra.getInt("outputX", mOutputX);
		mOutputY = extra.getInt("outputY", mOutputY);
		mOutputPath = extra.getString("output", "");

		mType = extra.getInt("type");
		switch (mType) {
		case TYPE_USER_HEAD:
			setTitle("选取头像");
			break;

		case TYPE_USER_COVER:
			setTitle("选取背景");
			break;

		default:
			setTitle("选取图像");
			break;
		}

		Uri imageUri = intent.getData();
		if (imageUri == null) {
//			ToastUtil.showToast(this, "参数错误");
			finish();

			return;
		}

		try {
			Options opts = new Options();
			opts.inJustDecodeBounds = true;

			BitmapFactory.decodeStream(
					getContentResolver().openInputStream(imageUri), null, opts);


			// 图片太小，提示重新选择
			boolean headTooSmall = mType == TYPE_USER_HEAD
					&& (opts.outHeight < minHeight || opts.outWidth < minWidth);
			boolean coverTooSmall = mType == TYPE_USER_HEAD
					&& (opts.outHeight < minHeight || opts.outWidth < minWidth);
			if (headTooSmall || coverTooSmall) {
				Intent data = new Intent();
				data.putExtra("qualified", false);
				setResult(RESULT_OK, data);
				finish();
				
				return;
			}

			Bitmap srcBitmap = null;

			// 图片分辨率相对屏幕分辨率的最大倍数
			float maxRatio = 2.0f;

			// 宽或高超过限定，进行缩放
			if (opts.outHeight > ScreenUtil.SCREEN_HEIGHT_PIXELS * maxRatio
					|| opts.outWidth > ScreenUtil.SCREEN_WIDTH_PIXELS * maxRatio) {

				int heightRatio = Math.round(opts.outHeight
						/ ScreenUtil.SCREEN_HEIGHT_PIXELS / maxRatio);

				int widthRatio = Math.round(opts.outWidth
						/ ScreenUtil.SCREEN_WIDTH_PIXELS / maxRatio);

				opts.inJustDecodeBounds = false;
				opts.inSampleSize = Math.max(heightRatio, widthRatio);

				srcBitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(imageUri), null, opts);
			} else {
				// 原始大小
				srcBitmap = BitmapFactory.decodeStream(getContentResolver()
						.openInputStream(imageUri));
			}

			// 判断是否需要对图像进行旋转处理


			int degree = ExifUtils.getExifOrientation(this, imageUri);
			if (degree != 0) {
				Bitmap resBitmap = BitmapUtils.rotateBitmap(srcBitmap, degree,
						true);
				mCropImageView.setImageBitmap(resBitmap);

			} else {
				// 不需要旋转处理
				mCropImageView.setImageBitmap(srcBitmap);
			}

			mImageLoaded = true;

		} catch (Exception e) {
			e.printStackTrace();
			
//			ToastUtil.showToast(this, "文件无法打开");
			
			finish();
			
			return;
		}

	}

	class CropHeadWorker extends AsyncTask<Void, Void, Void> {

		private Bitmap mBitmap;

		@Override
		protected void onPreExecute() {
//			showProgressDialog();
		}

		@Override
		protected Void doInBackground(Void... params) {

			Uri saveUri = Uri.parse(mOutputPath);

			// 裁剪
			mBitmap = mCropImageView.crop(mOutputX, mOutputY);

			// 保存到文件
			try {
				mBitmap.compress(mOutputFormat, 75, getContentResolver()
						.openOutputStream(saveUri));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

//			dismissProgressDialog();

			setResult(RESULT_OK);

			mBitmap.recycle();

			finish();
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
