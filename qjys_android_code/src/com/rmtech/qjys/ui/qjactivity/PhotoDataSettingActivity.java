package com.rmtech.qjys.ui.qjactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.view.MeItemLayout;

public class PhotoDataSettingActivity extends BaseActivity {
	
	public MeItemLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.qj_case_settings);
		setTitle("设置");
		setLeftTitle("返回");
		layout = (MeItemLayout) findViewById(R.id.clean);
		layout.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(PhotoDataSettingActivity.this, MeCleanMemoryActivity.class);
				intent.putExtra("from", "PhotoDataSettingActivity");
				startActivity(intent);
			}
		});
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
