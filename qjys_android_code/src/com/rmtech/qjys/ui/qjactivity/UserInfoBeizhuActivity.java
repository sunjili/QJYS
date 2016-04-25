package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
/***
 * 编辑备注    页面
 * @author Administrator
 *
 */
public class UserInfoBeizhuActivity extends BaseActivity {
	/**  姓名    */
	private EditText et_name;
	private String name;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_userinfo_beizhu);
		setTitle("备注信息");
		setRightTitle("保存", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				name=et_name.getText().toString().trim();
				if (!TextUtils.isEmpty(name)) {
					Intent data=new Intent();
					data.putExtra("string", name);
					setResult(UserInfoBeizhuActivity.RESULT_OK, data);
				}
				finish();
			}
		});
		initView();
	}

	private void initView() {
		et_name=(EditText) findViewById(R.id.et_name);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, UserInfoBeizhuActivity.class);
		context.startActivity(intent);
	}
}
