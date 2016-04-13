package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;

/***
 * 更换手机号 页面
 * 
 * @author Administrator
 * 
 */
public class MePhoneChangeActivity extends BaseActivity implements
		View.OnClickListener {
	private EditText et_phone;
	/**手机号*/
	private String phone;
	private EditText et_verify;
	/**验证码*/
	private String verify;
	private Button btn_get_verify;
	private Context context;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_phone_change);
		setTitle("更换手机号");
		context = MePhoneChangeActivity.this;
		setRightTitle("下一步", new OnClickListener() {

			@Override
			public void onClick(View v) {
				//TODO  校验验证码
				phone=et_phone.getText().toString().trim();
				verify=et_verify.getText().toString().trim();
				finish();
			}
		});
		initView();
	}

	private void initView() {
		btn_get_verify = (Button) findViewById(R.id.btn_get_verify);
		btn_get_verify.setOnClickListener(this);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_verify = (EditText) findViewById(R.id.et_verify);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MePhoneChangeActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_get_verify:
			//TODO 获取验证码
			Toast.makeText(context, "获取验证码", Toast.LENGTH_LONG).show();
			break;

		default:
			break;
		}
	}
}
