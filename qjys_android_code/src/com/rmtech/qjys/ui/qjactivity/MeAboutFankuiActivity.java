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
 * 反馈与建议 页面
 * 
 * @author Administrator
 * 
 */
public class MeAboutFankuiActivity extends BaseActivity {
	private EditText et_string;
	private EditText et_phone;
	/** 反馈内容 */
	private String string;
	/** 联系方式 */
	private String phone;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_about_fankui);
		setTitle("反馈建议");
		setRightTitle("发送", new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 发送建议
				string = et_string.getText().toString().trim();
				phone = et_phone.getText().toString().trim();
				finish();
			}
		});
		initView();
	}

	private void initView() {
		et_string = (EditText) this.findViewById(R.id.et_string);
		et_phone = (EditText) this.findViewById(R.id.et_phone);

	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeAboutFankuiActivity.class);
		context.startActivity(intent);
	}
}
