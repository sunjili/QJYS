package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;

/***
 * 奇迹医生-登录 页面
 * 
 * @author Administrator
 * 
 */
public class LoginPasswordActivity extends BaseActivity implements
		View.OnClickListener {
	private EditText et_phone;
	private EditText et_password;
	/**手机号*/
	private String phone;
	/**登录密码*/
	private String password;
	private Context context;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_login_with_password);
		setTitle("奇迹医生-登录");
		context = LoginPasswordActivity.this;
		setRightTitle("", null);
		initView();
	}

	private void initView() {
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_password = (EditText) findViewById(R.id.et_password);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, LoginPasswordActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
	}
}
