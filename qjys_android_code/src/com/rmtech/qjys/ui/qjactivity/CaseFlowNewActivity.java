package com.rmtech.qjys.ui.qjactivity;

import java.util.HashMap;

import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MFlowList.FlowInfo;
import com.rmtech.qjys.utils.FlowListManager;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.http.okhttp.OkHttpUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import okhttp3.Call;

/***
 * 新建临床诊疗规范及流程 页面
 * 
 * @author Administrator
 * 
 */
public class CaseFlowNewActivity extends MeFlowNewActivity {
	

	private CheckBox iv_right;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_flow_edit);
		isNew = true;
		findViewById(R.id.btn_delete).setVisibility(View.GONE);
		findViewById(R.id.bottom_layout).setVisibility(View.VISIBLE);
		et_title = (EditText) findViewById(R.id.et_title);
		setTextWhacher(CaseFlowNewActivity.this, et_title, 75);
		et_content = (EditText) findViewById(R.id.et_content);
		setTextWhacher(CaseFlowNewActivity.this, et_content, 8000);
		setTitle("编辑临床诊疗规范及流程");
		setLeftTitle("返回");
		iv_right = (CheckBox) findViewById(R.id.iv_right);
		// iv_right.setOnClickListener(this);
		iv_right.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				isSave = isChecked;
			}
		});
		setRightTitle("保存", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final String procedure_title = et_title.getText().toString()
						.trim();
				final String procedure_text = et_content.getText().toString()
						.trim();
				if (TextUtils.isEmpty(procedure_title)&&TextUtils.isEmpty(procedure_text)) {
					showDialog("请填写规范名称及规范内容");
					return;
				}
				if (TextUtils.isEmpty(procedure_title)&&!TextUtils.isEmpty(procedure_text)) {
					showDialog("请填写规范名称");
					return;
				}
				if (!TextUtils.isEmpty(procedure_title)&&TextUtils.isEmpty(procedure_text)) {
					showDialog("请填写规范内容");
					return;
				}
				final CaseInfo mCaseInfo = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(caseId);
				if (mCaseInfo != null) {
					if (isNew&&isSave) {
						saveAsPlate();
					}
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("procedure_title", procedure_title);
					params.put("procedure_text", procedure_text);
					params.put("patient_id", mCaseInfo.id);
					OkHttpUtils.post(QjHttp.URL_UPDATE_PATIENT, params, new BaseModelCallback() {

						@Override
						public void onError(Call call, Exception e) {

						}

						@Override
						public void onResponseSucces(MBase response) {

							CaseInfo caseInfo = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(mCaseInfo.id);
							if (caseInfo != null) {
								caseInfo.procedure_title = procedure_title;
								caseInfo.procedure_text = procedure_text;
							}
							setResult(RESULT_OK, new Intent());
							finish();
							Toast.makeText(getActivity(), "保存成功", 1)
									.show();
						}
					});
					return;
				}else {
					if (flowInfo == null) {
						flowInfo = new FlowInfo();
					}
					flowInfo.title = et_title.getText().toString().trim();
					flowInfo.procedure = et_content.getText().toString().trim();
					if (!TextUtils.isEmpty(flowInfo.id)) {
						FlowListManager.getInstance().updataFlowInfo(flowInfo);
					}
					setResult(RESULT_OK, new Intent().putExtra("FlowInfo",
							(Parcelable) flowInfo));
					Intent intent = new Intent();
					intent.setClass(getActivity(), CaseFlowDetailActivity.class);
					intent.putExtra("FlowInfo", (Parcelable) flowInfo);
					intent.putExtra("requestType", QjConstant.REQUEST_CODE_NEW_CASE_FLOW);
					intent.putExtra("from", "111");
					setCaseId(intent, caseId);
					getActivity().startActivityForResult(intent, requestType);
					CaseFlowNewActivity.this.finish();
					if (isNew&&isSave) {
						saveAsPlate();
					}
				}
			}
		});
	}
	
	protected void saveAsPlate() {
		flowInfo = new FlowInfo();
		flowInfo.title = et_title.getText().toString().trim();
		flowInfo.procedure = et_content.getText().toString().trim();
		FlowListManager.getInstance().addFlow(flowInfo,
				new BaseModelCallback() {

					@Override
					public void onResponseSucces(MBase response) {
//						 Toast.makeText(getActivity(), "保存成功", 1).show();
//						 getActivity().finish();
					}

					@Override
					public void onError(Call call, Exception e) {
						 Toast.makeText(getActivity(), "保存失败", 1).show();
					}
				});
	}

	public static void show(Activity context, String caseId, int type) {
		Intent intent = new Intent();
		intent.setClass(context, CaseFlowNewActivity.class);
		intent.putExtra("requestType", type);
		setCaseId(intent, caseId);
		context.startActivityForResult(intent, type);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_right:
			Toast.makeText(getActivity(), "保存为模板", Toast.LENGTH_SHORT).show();
			iv_right.setChecked(true);
			break;

		default:
			break;
		}
	}

}
