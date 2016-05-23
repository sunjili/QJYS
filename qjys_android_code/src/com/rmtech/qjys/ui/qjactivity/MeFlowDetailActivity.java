package com.rmtech.qjys.ui.qjactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.R;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.gson.MFlowList.FlowInfo;
import com.rmtech.qjys.utils.GroupAndCaseListManager;

/***
 * 临床诊疗规范及流程 详情页面
 * 
 * @author Administrator
 * 
 */
public class MeFlowDetailActivity extends MeFlowBaseActivity {
	protected Button btn_add_flow_detail;

	private TextView tv_title;
	private TextView tv_content;
	// private String title;
	// private String content;
	private Context context;
	private RelativeLayout rl_empty;
	protected FlowInfo flowInfo;

	protected CaseInfo getCaseInfo() {
		return GroupAndCaseListManager.getInstance()
				.getCaseInfoByCaseId(caseId);

	}

	@Override
	protected void onResume() {
		super.onResume();
		bindView();
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		flowInfo = getIntent().getParcelableExtra("FlowInfo");
		setContentView(R.layout.qj_me_flow_detail);
		setTitle("诊疗规范及流程");
		context = MeFlowDetailActivity.this;
		initView();
		setLeftTitle("返回");
		if (requestType != QjConstant.REQUEST_CODE_CASE_FLOW_LIST) {
			setRightTitle("编辑", clickListener);
		}
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch(requestType) {
			case QjConstant.REQUEST_CODE_ME_FLOW:
				FlowInfo newflowInfo = new FlowInfo();
				if (flowInfo != null) {
					newflowInfo.id = flowInfo.id;
				}
				newflowInfo.title = tv_title.getText().toString();
				newflowInfo.procedure = tv_content.getText().toString();
				MeFlowEditActivity.show(getActivity(), flowInfo,
						QjConstant.REQUEST_CODE_ME_FLOW);
				break;
			case QjConstant.REQUEST_CODE_NEW_CASE_FLOW:
			case QjConstant.REQUEST_CODE_EDIT_CASE_FLOW:
				CaseFlowEditActivity.show(getActivity(), caseId, flowInfo, requestType);
				break;
			}
		}
	};

	protected void initView() {

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_content = (TextView) findViewById(R.id.tv_content);

		btn_add_flow_detail = (Button) findViewById(R.id.btn_add_flow_detail);
		btn_add_flow_detail.setVisibility(View.GONE);
		rl_empty = (RelativeLayout) findViewById(R.id.rl_empty);
		// bindView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case QjConstant.REQUEST_CODE_ME_FLOW:
				FlowInfo tempflowInfo = data.getParcelableExtra("FlowInfo");
				if (tempflowInfo != null) {
					tv_title.setText(tempflowInfo.title);
					tv_content.setText(tempflowInfo.procedure);
					if (flowInfo != null) {
						flowInfo.title = tempflowInfo.title;
						flowInfo.procedure = tempflowInfo.procedure;
					}
				}
				break;
			}
		}
	}

	protected void bindView() {
		if (requestType == QjConstant.REQUEST_CODE_EDIT_CASE_FLOW) {
			CaseInfo tempcaseInfo = getCaseInfo();
			if (tempcaseInfo != null && tempcaseInfo.hasFlow()) {
				tv_title.setText(tempcaseInfo.procedure_title);
				tv_content.setText(tempcaseInfo.procedure_text);
				tv_title.setVisibility(View.VISIBLE);
				tv_content.setVisibility(View.VISIBLE);
				rl_empty.setVisibility(View.GONE);
				btn_add_flow_detail.setVisibility(View.GONE);
			} else {
				tv_title.setVisibility(View.GONE);
				tv_content.setVisibility(View.GONE);
				rl_empty.setVisibility(View.VISIBLE);
				btn_add_flow_detail.setVisibility(View.VISIBLE);
			}

		} else if (flowInfo != null && !flowInfo.isEmpty()) {
			tv_title.setText(flowInfo.title);
			tv_content.setText(flowInfo.procedure);
			rl_empty.setVisibility(View.GONE);
			btn_add_flow_detail.setVisibility(View.GONE);
		} else {
			tv_title.setVisibility(View.GONE);
			tv_content.setVisibility(View.GONE);
			rl_empty.setVisibility(View.VISIBLE);
			btn_add_flow_detail.setVisibility(View.VISIBLE);
		}
		if (requestType == QjConstant.REQUEST_CODE_ME_FLOW) {
			setRightTitle("编辑", clickListener);
		}
	}

	protected boolean showTitleBar() {
		return true;
	}

	// public static void show(Context context, String caseId) {
	// Intent intent = new Intent();
	// intent.setClass(context, MeFlowDetailActivity.class);
	// setCaseId(intent, caseId);
	// intent.putExtra("requestType", QjConstant.REQUEST_CODE_EDIT_CASE_FLOW);
	//
	// context.startActivity(intent);
	// }
	//
	public static void show(Activity context, FlowInfo item) {
		Intent intent = new Intent();
		intent.setClass(context, MeFlowDetailActivity.class);
		intent.putExtra("FlowInfo", (Parcelable) item);
		intent.putExtra("requestType", QjConstant.REQUEST_CODE_ME_FLOW);
		context.startActivityForResult(intent, QjConstant.REQUEST_CODE_ME_FLOW);
	}

}
