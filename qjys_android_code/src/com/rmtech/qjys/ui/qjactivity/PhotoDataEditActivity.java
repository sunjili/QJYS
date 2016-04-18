package com.rmtech.qjys.ui.qjactivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;

import uk.co.senab.photoview.PhotoView;

@SuppressLint("NewApi")
public class PhotoDataEditActivity extends BaseActivity implements OnClickListener {

	protected static final String TAG = PhotoDataEditActivity.class.getSimpleName();
	private PhotoView photoView;
	private RelativeLayout titleLayout;
	private TextView returnTv;
	private TextView titleTv;
	private TextView editTv;
	private RelativeLayout bottomLayout;
	private TextView mirrorTv;
	private TextView rotateTv;

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
		photoView.setImageResource(R.drawable.ic_launcher);

	}

	public static void show(Activity context) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataEditActivity.class);
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
