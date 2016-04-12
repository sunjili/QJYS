package com.rmtech.qjys.ui.qjactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;

public class EditCaseActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qjactivity_edit_case);
		setTitle("编辑病例");

	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context) {
		Intent intent = new Intent();
		intent.setClass(context, EditCaseActivity.class);
		context.startActivity(intent);
	}
}
