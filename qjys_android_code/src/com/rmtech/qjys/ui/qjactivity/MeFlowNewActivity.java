package com.rmtech.qjys.ui.qjactivity;

import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MFlowList.FlowInfo;
import com.rmtech.qjys.utils.FlowListManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import okhttp3.Call;

/***
 * 新建临床诊疗规范及流程 页面
 * 
 * @author Administrator
 * 
 */
public class MeFlowNewActivity extends MeFlowEditActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_flow_edit);
		isNew = true;
		findViewById(R.id.btn_delete).setVisibility(View.GONE);
		findViewById(R.id.bottom_layout).setVisibility(View.GONE);
		et_title = (EditText) findViewById(R.id.et_title);
		setTextWhacher(MeFlowNewActivity.this, et_title, 75);
		et_content = (EditText) findViewById(R.id.et_content);
		setTextWhacher(MeFlowNewActivity.this, et_content, 8000);
		setTitle("编辑临床诊疗规范及流程");
		setLeftTitle("返回");
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
				if (flowInfo == null) {
					flowInfo = new FlowInfo();
				}
				flowInfo.title = et_title.getText().toString().trim();
				flowInfo.procedure = et_content.getText().toString().trim();
				if (isNew) {
					saveAsPlate();
				}
			}
		});
	}
	
	protected void saveAsPlate() {
		FlowListManager.getInstance().addFlow(flowInfo,
				new BaseModelCallback() {

					@Override
					public void onResponseSucces(MBase response) {
						 Toast.makeText(getActivity(), "保存成功", 1).show();
						 getActivity().finish();
					}

					@Override
					public void onError(Call call, Exception e) {
						 Toast.makeText(getActivity(), "保存失败", 1).show();
					}
				});
	}

	public static void show(Activity context, String caseId, int type) {
		Intent intent = new Intent();
		intent.setClass(context, MeFlowNewActivity.class);
		intent.putExtra("requestType", type);
		setCaseId(intent, caseId);
		context.startActivityForResult(intent,type);
	}

}
