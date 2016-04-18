package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;

/***
 * 临床诊疗规范及流程 详情页面
 * 
 * @author Administrator
 * 
 */
public class MeFlowDetailActivity extends BaseActivity implements
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
		setTitle("诊疗规范及流程");
		context = MeFlowDetailActivity.this;
		initView();
		title = tv_title.getText().toString().trim();
		content = tv_content.getText().toString().trim();
		// if(!TextUtils.isEmpty(content)){
		// rl_empty.setVisibility(View.VISIBLE);
		// }else{
		rl_empty.setVisibility(View.GONE);
		setRightTitle("编辑", new OnClickListener() {

			@Override
			public void onClick(View v) {
				MeFlowEditActivity.show(context);
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
		intent.setClass(context, MeFlowDetailActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_flow_detail:
			MeFlowEditActivity.show(context);
			break;
		default:
			break;
		}
	}
}
