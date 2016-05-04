package com.rmtech.qjys.ui.qjactivity;

import java.util.HashMap;

import okhttp3.Call;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MFlowList.FlowInfo;

/***
 * 临床诊疗规范及流程 详情页面
 * 
 * @author Administrator
 * 
 */
public class CaseFlowDetailActivity extends MeFlowDetailActivity implements
		View.OnClickListener {
	@Override
	protected void onCreate(Bundle arg0) {

		super.onCreate(arg0);
		if (requestType == QjConstant.REQUEST_CODE_NEW_CASE_FLOW) {
			if (flowInfo == null || flowInfo.isEmpty()) {
				CaseFlowSelectorActivity.show(CaseFlowDetailActivity.this);
				return;
			}
		} else if (requestType == QjConstant.REQUEST_CODE_CASE_FLOW_LIST) {
			View userbutton = findViewById(R.id.use_button);
			userbutton.setVisibility(View.VISIBLE);
			userbutton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!TextUtils.isEmpty(caseId) && getCaseInfo() != null) {
						HashMap<String, String> params = new HashMap<String, String>();
						params.put("patient_id", caseId);
						params.put("procedure_title", flowInfo.title);
						params.put("procedure_text", flowInfo.procedure);
						QjHttp.updateUserinfo(params, new BaseModelCallback() {

							@Override
							public void onError(Call call, Exception e) {
								Toast.makeText(getActivity(), "保存失败",
										Toast.LENGTH_LONG).show();
							}

							@Override
							public void onResponseSucces(MBase response) {
								Toast.makeText(getActivity(), "保存成功",
										Toast.LENGTH_LONG).show();
								CaseInfo tempCaseInfo = getCaseInfo();
								if (tempCaseInfo != null) {
									tempCaseInfo.procedure_title = flowInfo.title;
									tempCaseInfo.procedure_text = flowInfo.procedure;
								}
								onFinish();
							}

						});
					} else {
						onFinish();
					}
				}
			});
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case QjConstant.REQUEST_CODE_NEW_CASE_FLOW:
			case QjConstant.REQUEST_CODE_EDIT_CASE_FLOW:
				setResult(RESULT_OK, data);
				finish();
				break;
			}
		}
	}

	// @Override
	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// super.onActivityResult(requestCode, resultCode, data);
	// if (resultCode == RESULT_OK) {
	// switch (requestCode) {
	// case QjConstant.REQUEST_CODE_NEW_CASE_FLOW:
	// setResult(RESULT_OK, data);
	// finish();
	// break;
	// }
	// }
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_flow_detail:
			CaseFlowSelectorActivity.show(CaseFlowDetailActivity.this, caseId);
			break;
		default:
			break;
		}
	}

	@Override
	protected void initView() {
		super.initView();
		btn_add_flow_detail.setOnClickListener(this);
	}

	public void onFinish() {
		Intent data = new Intent();
		data.putExtra("FlowInfo", (Parcelable) flowInfo);
		setResult(RESULT_OK, data);
		finish();
	}

	public static void show(Activity context, String caseId, FlowInfo item,
			int type) {
		Intent intent = new Intent();
		intent.setClass(context, CaseFlowDetailActivity.class);
		intent.putExtra("FlowInfo", (Parcelable) item);
		intent.putExtra("requestType", type);
		setCaseId(intent, caseId);
		context.startActivityForResult(intent, type);
	}

}
