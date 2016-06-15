package com.rmtech.qjys.ui.qjactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;

import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.R;
import com.rmtech.qjys.model.gson.MFlowList.FlowInfo;
import com.umeng.analytics.MobclickAgent;

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
		setLeftTitle("返回");
		setRightTitle(R.drawable.btn_case_new, new OnClickListener() {

			@Override
			public void onClick(View v) {
				CaseFlowNewActivity.show(getActivity(), caseId, requestType);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			setResult(RESULT_OK, data);
			finish();
		}
	}

	@Override
	protected void onFlowItemClick(final FlowInfo item) {
		if(requestType == QjConstant.REQUEST_CODE_NEW_CASE_FLOW){
//			CaseFlowDetailActivity.show(getActivity(), caseId, item,
//					QjConstant.REQUEST_CODE_NEW_CASE_FLOW);
			Intent intent = new Intent();
			intent.setClass(getActivity(), CaseFlowDetailActivity.class);
			intent.putExtra("FlowInfo", (Parcelable) item);
			intent.putExtra("requestType", requestType);
			intent.putExtra("from", "caseSelector");
			setCaseId(intent, caseId);
			getActivity().startActivityForResult(intent, requestType);
		}else{
			CaseFlowDetailActivity.show(getActivity(), caseId, item,
					QjConstant.REQUEST_CODE_CASE_FLOW_LIST);
		}
	}

	public static void show(Activity context, String caseId) {
		Intent intent = new Intent();
		intent.setClass(context, CaseFlowSelectorActivity.class);
		intent.putExtra("requestType", QjConstant.REQUEST_CODE_EDIT_CASE_FLOW);// (context,
		setCaseId(intent, caseId);
		context.startActivityForResult(intent, QjConstant.REQUEST_CODE_EDIT_CASE_FLOW);
	}
	
	public static void show(Activity context, String caseId, int requstType) {
		Intent intent = new Intent();
		intent.setClass(context, CaseFlowSelectorActivity.class);
		intent.putExtra("requestType", requstType);// (context,
		setCaseId(intent, caseId);
		context.startActivityForResult(intent, requstType);
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
	
	@Override
	protected void onResume() {
		super.onResume();
		 MobclickAgent.onResume(this);
	}
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 MobclickAgent.onPause(this);
	}
}
