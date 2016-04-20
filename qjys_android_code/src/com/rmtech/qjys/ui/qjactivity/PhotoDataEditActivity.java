package com.rmtech.qjys.ui.qjactivity;

import uk.co.senab.photoview.PhotoView;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.R;
import com.rmtech.qjys.model.PhotoDataInfo;
import com.rmtech.qjys.ui.BaseActivity;

@SuppressLint("NewApi")
public class PhotoDataEditActivity extends BaseActivity implements
		OnClickListener {

	protected static final String TAG = PhotoDataEditActivity.class
			.getSimpleName();
	private PhotoView photoView;
	private RelativeLayout titleLayout;
	private TextView returnTv;
	private TextView titleTv;
	private TextView editTv;
	private RelativeLayout bottomLayout;
	private TextView mirrorTv;
	private TextView rotateTv;
	DisplayImageOptions optionsThumb = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.default_error)
			.showImageOnFail(R.drawable.default_error)
			.resetViewBeforeLoading(true).cacheOnDisk(true).cacheInMemory(true)
			.build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_photo_edit);

		photoView = (PhotoView) findViewById(R.id.photoView);
		titleLayout = (RelativeLayout) findViewById(R.id.title_layout);
		returnTv = (TextView) findViewById(R.id.return_tv);
		titleTv = (TextView) findViewById(R.id.title_tv);
		editTv = (TextView) findViewById(R.id.edit_tv);
		bottomLayout = (RelativeLayout) findViewById(R.id.bottom_layout);
		mirrorTv = (TextView) findViewById(R.id.mirror_tv);
		mirrorTv.setOnClickListener(this);
		rotateTv = (TextView) findViewById(R.id.rotate_tv);
		rotateTv.setOnClickListener(this);
		PhotoDataInfo photoData = getIntent().getParcelableExtra("photo_data");
		if (photoData == null) {
			return;
		}
		ImageLoader.getInstance().displayImage(photoData.origin_url, photoView,
				optionsThumb);
	}

	public static void show(Activity context, PhotoDataInfo photoData) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataEditActivity.class);
		intent.putExtra("photo_data", (Parcelable) photoData);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mirror_tv:
			photoView.setMirror();
			break;
		case R.id.rotate_tv:
			photoView.setRotationBy(90);
			break;
		}

	}

}
