package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;

public class AddCaseActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qjactivity_add_case);
		setTitle("添加病例");
		findViewById(R.id.photo_data_layout).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PhotoDataManagerActivity.show(AddCaseActivity.this);
			}
		});

	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, AddCaseActivity.class);
		context.startActivity(intent);
	}
}
