package com.rmtech.qjys.ui.qjactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;

public class PhotoDataSettingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_photo_manager);
		setTitle("文件设置");
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context) {
		Intent intent = new Intent();
		intent.setClass(context, PhotoDataSettingActivity.class);
		context.startActivity(intent);
	}

}
