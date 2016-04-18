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
 * 编辑病房号    页面
 * @author Administrator
 *
 */
public class CaseRoomNumberActivity extends BaseActivity {
	/**  病房号   */
	private EditText et_room_number;
	private String roomNumber;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_case_room_number);
		setTitle("病房号");
		setRightTitle("保存", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				roomNumber=et_room_number.getText().toString().trim();
				finish();
			}
		});
		initView();
	}

	private void initView() {
		et_room_number=(EditText) findViewById(R.id.et_room_number);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, CaseRoomNumberActivity.class);
		context.startActivity(intent);
	}
}
