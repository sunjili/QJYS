package com.rmtech.qjys.ui.qjactivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.event.CaseEvent;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.gson.MFlowList.FlowInfo;
import com.rmtech.qjys.utils.GroupAndCaseListManager;

/***
 * 临床诊疗规范及流程 详情页面
 * 
 * @author Administrator
 * 
 */
public class MeFlowDetailActivity extends MeFlowBaseActivity implements View.OnClickListener {
	private Button btn_add_flow_detail;

	private TextView tv_title;
	private TextView tv_content;
	// private String title;
	// private String content;
	private Context context;
	private RelativeLayout rl_empty;
	private FlowInfo flowInfo;


	private CaseInfo getCaseInfo() {
		return GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(caseId);

	}

	@Override
	protected void onResume() {
		super.onResume();
		bindView();
	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_flow_detail);
		setTitle("诊疗规范及流程");
		context = MeFlowDetailActivity.this;
		flowInfo = getIntent().getParcelableExtra("FlowInfo");
//		if (!TextUtils.isEmpty(caseId) && getCaseInfo() != null) {
//			isInCaseModel = true;
//		}

		initView();
		// title = tv_title.getText().toString().trim();
		// content = tv_content.getText().toString().trim();
		// if(!TextUtils.isEmpty(content)){
		// rl_empty.setVisibility(View.VISIBLE);
		// }else{
		setLeftTitle("返回");

	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
//			if (isInCaseModel) {
				// FlowInfo flowInfo = new FlowInfo();
				// flowInfo.title = getCaseInfo().procedure_title;
				// flowInfo.procedure = getCaseInfo().procedure_text;
				MeFlowEditActivity.show(getActivity(), caseId, requestType);
//			}
		}
	};

	private void initView() {

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_content = (TextView) findViewById(R.id.tv_content);

		btn_add_flow_detail = (Button) findViewById(R.id.btn_add_flow_detail);
		btn_add_flow_detail.setOnClickListener(this);

		rl_empty = (RelativeLayout) findViewById(R.id.rl_empty);
		// bindView();
	}

	private void bindView() {
		CaseInfo tempcaseInfo = getCaseInfo();
		FlowInfo newFlowInfo = new FlowInfo();
		if(tempcaseInfo != null && tempcaseInfo.hasFlow()) {
			newFlowInfo.title = tempcaseInfo.procedure_title;
			newFlowInfo.procedure = tempcaseInfo.procedure_text;
		} else if(flowInfo != null && !flowInfo.isEmpty()) {
			newFlowInfo.title = flowInfo.title;
			newFlowInfo.procedure = flowInfo.procedure;
		}
		
		if (newFlowInfo.isEmpty()) {
			tv_title.setVisibility(View.GONE);
			tv_content.setVisibility(View.GONE);
			rl_empty.setVisibility(View.VISIBLE);
			btn_add_flow_detail.setVisibility(View.VISIBLE);

		} else {
			setRightTitle("编辑", clickListener);
			tv_title.setText(newFlowInfo.title);
			tv_content.setText(newFlowInfo.procedure);
			rl_empty.setVisibility(View.GONE);
			btn_add_flow_detail.setVisibility(View.GONE);
		}
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context, String caseId) {
		Intent intent = new Intent();
		intent.setClass(context, MeFlowDetailActivity.class);
		setCaseId(intent, caseId);
		intent.putExtra("requestType", QjConstant.REQUEST_CODE_EDIT_CASE_FLOW);

		context.startActivity(intent);
	}

	public static void show(Context context, FlowInfo item, int type) {
		Intent intent = new Intent();
		intent.setClass(context, MeFlowDetailActivity.class);
		intent.putExtra("FlowInfo", (Parcelable) item);
		intent.putExtra("requestType", type);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_flow_detail:
			if (TextUtils.isEmpty(caseId) && getCaseInfo() != null) {
				MeFlowActivity.show(getActivity(), caseId);
			} else {
				MeFlowActivity.show(getActivity(), caseId);
//				MeFlowEditActivity.show(getActivity(), QjConstant.REQUEST_CODE_NEW_FLOW);
			}
			break;
		default:
			break;
		}
	}
}
