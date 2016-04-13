package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;

/***
 * 清理內存 页面
 * 
 * @author Administrator
 * 
 */
public class MeCleanMemoryActivity extends BaseActivity implements
		OnClickListener {
	private Button btn_clean;
	private Context context;
	private TextView tv_memory_value;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_clean_memory);
		setTitle("清理存储空间");
		context = MeCleanMemoryActivity.this;
		initView();
	}

	private void initView() {
		btn_clean = (Button) findViewById(R.id.btn_clean);
		btn_clean.setOnClickListener(this);
		tv_memory_value = (TextView) this.findViewById(R.id.tv_memory_value);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeCleanMemoryActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_clean:
			Toast.makeText(context, "一键清理", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}
}
