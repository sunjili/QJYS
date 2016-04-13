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
 * 设置新密码 页面
 * 
 * @author Administrator
 * 
 */
public class MePasswordNewActivity extends BaseActivity implements
		View.OnClickListener {
	private EditText et_password_1;
	private EditText et_password_2;
	/**密码1*/
	private String password1;
	/**确认密码*/
	private String password2;
	private Context context;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_password_new);
		setTitle("设置密码");
		context = MePasswordNewActivity.this;
		setRightTitle("完成", new OnClickListener() {

			@Override
			public void onClick(View v) {
			
				password1=et_password_1.getText().toString().trim();
				password2=et_password_2.getText().toString().trim();
				if (!TextUtils.isEmpty(password1)&&!TextUtils.isEmpty(password2)&&password1.equals(password2)) {
					//TODO  保存新密码到服务器
					finish();
				}
			}
		});
		initView();
	}

	private void initView() {
		et_password_1 = (EditText) findViewById(R.id.et_password_1);
		et_password_2 = (EditText) findViewById(R.id.et_password_2);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MePasswordNewActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
	}
}
