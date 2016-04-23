package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;

/***
 * 编辑就诊 页面
 * 
 * @author Administrator
 * 
 */
public class CaseDiagnoseActivity extends BaseActivity implements
		View.OnClickListener {
	private LinearLayout ll_layout;
	private EditText et_diagnose;
	private LinearLayout ll_diagnose_add;
	private Context context;
	private String stringExtra;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_case_diagnose);
		setTitle("诊断");
		context = CaseDiagnoseActivity.this;
		setRightTitle("保存", new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!TextUtils.isEmpty(stringExtra)) {
					Intent data = new Intent();
					data.putExtra("string", stringExtra);
					setResult(MeNameActivity.RESULT_OK, data);
				}
				finish();
			}
		});
		initView();
		stringExtra = getIntent().getStringExtra("string");
		if(!TextUtils.isEmpty(stringExtra)){
			setViewValue(stringExtra);
		}
	}

	private void setViewValue(String tempStr) {
		ll_layout.removeAllViews();
		if (TextUtils.isEmpty(tempStr)) {
		} else {
			String[] strings = tempStr.split("&&");
			for (int i = 0; i < strings.length; i++) {
				View view = LayoutInflater.from(context).inflate(
						R.layout.qj_case_diagnose_item, null);
				TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
				tv_name.setText(strings[i]);
				ll_layout.addView(view);
			}
		}
	}

	private void initView() {
		ll_layout = (LinearLayout) findViewById(R.id.ll_layout);
		et_diagnose = (EditText) findViewById(R.id.et_diagnose);
		ll_diagnose_add = (LinearLayout) findViewById(R.id.ll_diagnose_add);
		ll_diagnose_add.setOnClickListener(this);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, CaseDiagnoseActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_diagnose_add:
			String trim = et_diagnose.getText().toString().trim();
			if (!TextUtils.isEmpty(trim)) {
				if(!TextUtils.isEmpty(stringExtra)){
					stringExtra = stringExtra + "&&" + trim;
				}else{
					stringExtra=trim;
				}
				setViewValue(stringExtra);
				et_diagnose.setText("");
			}
			break;

		default:
			break;
		}
	}
}
