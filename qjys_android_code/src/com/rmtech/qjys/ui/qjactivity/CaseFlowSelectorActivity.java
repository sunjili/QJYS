package com.rmtech.qjys.ui.qjactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.model.gson.MFlowList.FlowInfo;

/***
 * 新建 编辑 病例时 设置临床诊疗规范及流程 页面
 * 
 * @author Administrator
 * 
 */
public class CaseFlowSelectorActivity extends MeFlowActivity implements
		View.OnClickListener {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			// case QjConstant.REQUEST_CODE_EDIT_CASE_FLOW:
			// finish();
			// break;
			case QjConstant.REQUEST_CODE_CASE_FLOW_LIST:
				setResult(RESULT_OK, data);
				finish();
				break;
			}
		}
	}

	@Override
	protected void onFlowItemClick(final FlowInfo item) {
		CaseFlowDetailActivity.show(getActivity(), caseId, item,
				QjConstant.REQUEST_CODE_CASE_FLOW_LIST);
	}

	public static void show(Activity context, String caseId) {
		Intent intent = new Intent();
		intent.setClass(context, CaseFlowSelectorActivity.class);
		intent.putExtra("requestType", QjConstant.REQUEST_CODE_EDIT_CASE_FLOW);// (context,
		setCaseId(intent, caseId);
		context.startActivityForResult(intent, QjConstant.REQUEST_CODE_EDIT_CASE_FLOW);
	}

	public static void show(Activity context) {
		Intent intent = new Intent();
		intent.setClass(context, CaseFlowSelectorActivity.class);
		intent.putExtra("requestType", QjConstant.REQUEST_CODE_NEW_CASE_FLOW);// (context,
		context.startActivityForResult(intent, QjConstant.REQUEST_CODE_NEW_CASE_FLOW);

	}

	@Override
	public void onClick(View v) {
	}
}
