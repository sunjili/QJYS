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
 * 编辑科室    页面
 * @author Administrator
 *
 */
public class MeRoomActivity extends BaseActivity {
	private EditText et_name;
	private String roomName;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_room);
		setTitle("科室");
		setRightTitle("保存", new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				roomName=et_name.getText().toString().trim();
				if (!TextUtils.isEmpty(roomName)) {
					Intent data=new Intent();
					data.putExtra("string", roomName);
					setResult(MeNameActivity.RESULT_OK, data);
				}
				finish();
			
			}
		});
		initView();
	}

	private void initView() {
		et_name=(EditText) findViewById(R.id.et_room_name);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeRoomActivity.class);
		context.startActivity(intent);
	}
}
