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
 * 编辑病床号    页面
 * @author Administrator
 *
 */
public class CaseBedNumberActivity extends BaseActivity {
	/**  床号    */
	private EditText et_bed;
	private String bed;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_case_bed);
		setTitle("病床号");
		setRightTitle("保存", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				bed=et_bed.getText().toString().trim();
				if(!TextUtils.isEmpty(bed)){
					Intent data = new Intent();
					data.putExtra("string", bed);
					setResult(MeNameActivity.RESULT_OK, data);
					finish();
				}
			}
		});
		initView();
	}

	private void initView() {
		et_bed=(EditText) findViewById(R.id.et_bed);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, CaseBedNumberActivity.class);
		context.startActivity(intent);
	}
}
