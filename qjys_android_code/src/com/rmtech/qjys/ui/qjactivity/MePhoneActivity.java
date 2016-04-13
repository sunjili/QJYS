package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;

/***
 * 编辑手机号 页面
 * 
 * @author Administrator
 * 
 */
public class MePhoneActivity extends BaseActivity implements
		View.OnClickListener {
	private TextView tv_phone;
	private Button btn_change_phone;
	private Context context;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_phone);
		setTitle("手机号");
		context=MePhoneActivity.this;
		setRightTitle("", null);
		initView();
	}

	private void initView() {
		btn_change_phone = (Button) findViewById(R.id.btn_change_phone);
		btn_change_phone.setOnClickListener(this);
		tv_phone=(TextView) findViewById(R.id.tv_phone);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MePhoneActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_change_phone:
			Intent intent =new Intent(context,MePhoneChangeActivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}
}
