package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;

/***
 * 摘要
 * 
 * @author Administrator
 * 
 */
public class MeAbstractActivity extends BaseActivity implements
		View.OnClickListener {
	private Button btn_add_flow_detail;

	private TextView tv_title;
	private TextView tv_content;
	private String title;
	private String content;
	private Context context;
	private RelativeLayout rl_empty;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_flow_detail);
		setTitle("病例摘要");
		context = MeAbstractActivity.this;
		initView();
		title = tv_title.getText().toString().trim();
		content = tv_content.getText().toString().trim();
		// if(!TextUtils.isEmpty(content)){
		// rl_empty.setVisibility(View.VISIBLE);
		// }else{
		rl_empty.setVisibility(View.GONE);
		setLeftTitle("");
		setRightTitle("编辑", new OnClickListener() {

			@Override
			public void onClick(View v) {
//				MeFlowEditActivity.show(context);
			}
		});
		// }
	}

	private void initView() {

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_content = (TextView) findViewById(R.id.tv_content);

		btn_add_flow_detail = (Button) findViewById(R.id.btn_add_flow_detail);
		btn_add_flow_detail.setOnClickListener(this);
		rl_empty = (RelativeLayout) findViewById(R.id.rl_empty);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeAbstractActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_flow_detail:
//			MeFlowEditActivity.show(context);
			break;
		default:
			break;
		}
	}
}
