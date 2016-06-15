package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
import com.umeng.analytics.MobclickAgent;

public class SetPasswordActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_qj_set_pw);
		setTitle("奇迹医生－登录");

	}

	protected boolean showTitleBar() {
		return true;
	}

	public void setPasswordClick(View view) {

	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, SetPasswordActivity.class);
		context.startActivity(intent);
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
