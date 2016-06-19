package com.rmtech.qjys.ui.qjactivity;

import okhttp3.Call;

import org.greenrobot.eventbus.EventBus;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.event.PhotoDataEvent;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.model.gson.MUploadImageInfo;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
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
		if(!TextUtils.isEmpty(photoData.localPath)) {
			ImageLoader.getInstance().displayImage("file://" + photoData.localPath, photoView, optionsThumb);
		} else {
			ImageLoader.getInstance().displayImage(photoData.origin_url, photoView, optionsThumb);
		}
		caseInfo = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(caseId);
		if(caseInfo != null) {
			titleTv.setText(caseInfo.name);
		}
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
	boolean isDebug = false;
	private float mRotation = -90;
	private boolean isMirror = false;
	private ProgressDialog mProgressDialog;
	
	private void dismissDialog() {
		try {
			if(mProgressDialog != null) {
				mProgressDialog.dismiss();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.return_tv:
			finish();
			break;
		case R.id.mirror_tv:
			photoView.setMirror();
			isMirror = !isMirror;
			break;
		case R.id.rotate_tv:
//			if(isMirror) {
//				mProgressDialog = ProgressDialog.show(getActivity(), null, "处理中");
//				isMirror = false;
//				new AsyncTask<Void, Void, Bitmap>(){
//
//					@Override
//					protected Bitmap doInBackground(Void... params) {
//						
//						
//						return null;
//					}
//					
//				}.execute();
				
//			}
//			if(!TextUtils.isEmpty(photoData.localPath)) {
//				ImageLoader.getInstance().displayImage("file://" + photoData.localPath, photoView, optionsThumb);
//			} else {
//				ImageLoader.getInstance().displayImage(photoData.origin_url, photoView, optionsThumb);
//			}
//			if(mRotation%360 == -90) {
//				photoView.setBaseRotation(-90);
//			}
			photoView.setRotationTo(mRotation);
			mRotation  += -90;
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
			
			ImageLoadingListener listener = new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1,final Bitmap bitmap) {
					new AsyncTask<Void, Void, String>(){

						@Override
						protected String doInBackground(Void... params) {
							Matrix matrix = new Matrix(photoView.getSuppMatrix());
							Bitmap finalBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
							 
//							Log.d("sssssssssss","degree = "+degree);
//							Bitmap newbitmap = null;
//							if(isMirror) {
//						  
//						        Matrix mirrormatrix = new Matrix(); 
//						        mirrormatrix.postScale(-1, 1);
//						        newbitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mirrormatrix, true);
//								
//
//							} else {
//								newbitmap = bitmap;
//							}
//
//							Bitmap finalBitmap = newbitmap;
//							int degree = (int) (mRotation % 360);
//							if(degree!= 0 ) {
//								Matrix degreematrix = new Matrix();
//								degreematrix.setRotate(degree);
//								finalBitmap = Bitmap.createBitmap(newbitmap, 0, 0, newbitmap.getWidth(), newbitmap.getHeight(), degreematrix, false);
//								newbitmap.recycle();
//							}

							String path = FileUtils.saveImage(getActivity(), finalBitmap);
							if (TextUtils.isEmpty(path)) {
								pd.dismiss();
								return null;
							}
							
							return path;
						}

						@Override
						protected void onPostExecute(final String path) {
							super.onPostExecute(path);
							if(isDebug) {
								pd.dismiss();
								return ;
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
						
						}}.execute();
					
					
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
			};
			if(!TextUtils.isEmpty(photoData.localPath)) {
				ImageLoader.getInstance().loadImage("file://" + photoData.localPath, listener);
			} else {
				ImageLoader.getInstance().loadImage(photoData.origin_url, listener);
			}
			
//			Bitmap bitmap = convertViewToBitmap(photoView);

			
			// }

			// }.execute();
			break;
		}

	}

	public Bitmap convertViewToBitmap(PhotoView view) {
		
		
		
		Matrix matrix = view.getDrawMatrix();
//		Log.d("sssssssssss","getDrawMatrix = "+matrix.toString());
//		Log.d("sssssssssss","getSuppMatrix = "+view.getSuppMatrix().toString());
//		Log.d("sssssssssss","getBaseMatrix = "+view.getBaseMatrix().toString());
//		Log.d("sssssssssss","getDisplayMatrix = "+view.getDisplayMatrix().toString());
		ImageView imageview = view.getImageView();
	
//		ImageLoader.getInstance().loadImage(photoData.origin_url, new ImageLoadingListener() );
		Drawable drawable = imageview.getDrawable();
		Log.d("sssssssssss","getImageViewHeight = "+PhotoViewAttacher.getImageViewHeight(imageview));//.get.getDisplayMatrix().toString());
		Log.d("sssssssssss","getImageViewWidth = "+PhotoViewAttacher.getImageViewWidth(imageview));//.get.getDisplayMatrix().toString());
		Log.d("sssssssssss","drawable.getIntrinsicWidth() = "+drawable.getIntrinsicWidth());
		Log.d("sssssssssss","drawable.getIntrinsicHeight() = "+drawable.getIntrinsicHeight());

		Matrix mBaseMatrix = new Matrix(view.getBaseMatrix());
		int degree = (int) (mRotation % 360);
		Log.d("sssssssssss","degree = "+degree);
		mBaseMatrix.setRotate(degree);
		if(isMirror) {
			mBaseMatrix.setScale(-1, 1);
		}
//		matrix.getValues(values );
//		Log.d("sssssssssss","mBaseMatrix = "+mBaseMatrix.toString());
//		ImageLoader.getInstance().getMemoryCache().get(arg0);
//		Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
		
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
