package com.rmtech.qjys.ui.qjactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.rmtech.qjys.R;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.GroupDetailsActivity;
import com.rmtech.qjys.utils.GroupAndCaseListManager;

/***
 * 摘要
 * 
 * @author Administrator
 * 
 */
public class CaseAbstractActivity extends CaseWithIdActivity {

	private TextView tv_content;
	private View empty_img;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_case_abs_detail);
		setTitle("病例摘要");
		initView();
		setLeftTitle("");
		setRightTitle("编辑", new OnClickListener() {

			@Override
			public void onClick(View v) {
				CaseAbstractEditActivity.show(getActivity(), caseInfo);
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (caseInfo != null) {
			CaseInfo tempCase = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(caseInfo.id);
			if (tempCase != null) {
				caseInfo = tempCase;
			}
		}
		setValue();
	}

	private void setValue() {

		if (caseInfo != null && !TextUtils.isEmpty(caseInfo.abs)) {
			tv_content.setText(caseInfo.abs);
			tv_content.setVisibility(View.VISIBLE);
			empty_img.setVisibility(View.GONE);
		} else {
			tv_content.setVisibility(View.GONE);
			empty_img.setVisibility(View.VISIBLE);
		}
	}

	private void initView() {

		tv_content = (TextView) findViewById(R.id.content_tv);

		empty_img = findViewById(R.id.empty_img);

	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context, CaseInfo caseInfo) {
		Intent intent = new Intent();
		setCaseInfo(intent, caseInfo);
		intent.setClass(context, CaseAbstractActivity.class);
		context.startActivity(intent);
	}

}