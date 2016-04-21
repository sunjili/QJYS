package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
	private String phone;
	public static final int REQUEST_PHONE_CHANGE = 0x1111;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_phone);
		setTitle("手机号");
		context = MePhoneActivity.this;
		setRightTitle("", null);
		phone=getIntent().getStringExtra("string");
		initView();
	}

	private void initView() {
		btn_change_phone = (Button) findViewById(R.id.btn_change_phone);
		btn_change_phone.setOnClickListener(this);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		try {
			tv_phone.setText(phone.subSequence(0, 3)+"****"+phone.subSequence(8, phone.length()));
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			Intent intent = new Intent(context, MePhoneChangeActivity.class);
			startActivityForResult(intent, REQUEST_PHONE_CHANGE);
			break;

		default:
			break;
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case REQUEST_PHONE_CHANGE:
			if(resultCode==MePhoneActivity.RESULT_OK){
			setResult(MePhoneActivity.RESULT_OK, data);
			}
			finish();
			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
