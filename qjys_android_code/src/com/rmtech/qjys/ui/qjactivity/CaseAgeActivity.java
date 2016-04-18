package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
/***
 * 编辑年龄    页面
 * @author Administrator
 *
 */
public class CaseAgeActivity extends BaseActivity {
	/**  年龄    */
	private EditText et_age;
	private String age;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_case_age);
		setTitle("年龄");
		setRightTitle("保存", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				age=et_age.getText().toString().trim();
				finish();
			}
		});
		initView();
	}

	private void initView() {
		et_age=(EditText) findViewById(R.id.et_age);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, CaseAgeActivity.class);
		context.startActivity(intent);
	}
}
