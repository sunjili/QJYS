package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;

/***
 * 编辑临床诊疗规范及流程 页面
 * 
 * @author Administrator
 * 
 */
public class MeFlowEditActivity extends BaseActivity implements
		View.OnClickListener {
	private EditText et_title;
	private EditText et_content;
	private Button btn_delete;
	private String title;
	private String content;
	private Context context;
	private ImageView iv_right;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_flow_edit);
		setTitle("编辑临床诊疗规范及流程");
		context = MeFlowEditActivity.this;
		setRightTitle("保存", new OnClickListener() {

			@Override
			public void onClick(View v) { 
				title=et_title.getText().toString().trim();
				content=et_content.getText().toString().trim();
				if(!TextUtils.isEmpty(title)&&!TextUtils.isEmpty(content)){
					//TODO 保存
				MeFlowEditActivity.this.finish();
				}
				
			}
		});
		initView();
	}

	private void initView() {
		et_title = (EditText) findViewById(R.id.et_title);
		et_content = (EditText) findViewById(R.id.et_content);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		btn_delete.setOnClickListener(this);
		iv_right=(ImageView) findViewById(R.id.iv_right);
		iv_right.setOnClickListener(this);

	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeFlowEditActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_delete:
			Toast.makeText(context, "删除此规范", Toast.LENGTH_SHORT).show();
			break;
		case R.id.iv_right:
			Toast.makeText(context, "保存为模板", Toast.LENGTH_SHORT).show();
			break;
			
		default:
			break;
		}
	}
}
