package com.rmtech.qjys.ui.qjactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.view.MeItemLayout;

/***
 * 详细资料 页面
 * 
 * @author Administrator
 * 
 */
public class UserInfoActivity extends BaseActivity implements OnClickListener {
	public static final int USER_BEIZHU = 0x5666;
	private MeItemLayout user_beizhu;
	private String beizhu;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_userinfo);
		setTitle("详细资料");
		setRightTitle("", null);
		initView();
	}

	private void initView() {
		user_beizhu = (MeItemLayout) findViewById(R.id.user_beizhu);
		user_beizhu.setOnClickListener(this);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, UserInfoActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.user_beizhu:
			Intent intent = new Intent(this, UserInfoBeizhuActivity.class);
			startActivityForResult(intent, USER_BEIZHU);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case USER_BEIZHU:
			if (resultCode == Activity.RESULT_OK) {
				beizhu = data.getStringExtra("string");
				setViewValue();
			}
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void setViewValue() {
		user_beizhu.setRightText(beizhu);
	}
}
