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
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MFlowList.FlowInfo;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.http.okhttp.OkHttpUtils;
import com.umeng.analytics.MobclickAgent;

/***
 * 临床诊疗规范及流程 详情页面
 * 
 * @author Administrator
 * 
 */
/**
 * @author Homicide
 *
 */
public class CaseFlowDetailActivity extends MeFlowDetailActivity implements
		View.OnClickListener {
	@Override
	protected void onCreate(Bundle arg0) {

		super.onCreate(arg0);
		if(getCaseInfo()==null){//新建
			if (requestType == QjConstant.REQUEST_CODE_NEW_CASE_FLOW ) {
				if ((getIntent().getStringExtra("from")!=null&&getIntent().getStringExtra("from").equals("caseSelector"))
						|| flowInfo == null || flowInfo.isEmpty()) {
					setRightTitle("", null);
				}else {
					setRightTitle("编辑", new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							CaseFlowEditActivity.show(getActivity(), caseId, flowInfo, requestType);
						}
					});
				}
			} 
		}else if(!UserContext.getInstance().isMyself(getCaseInfo().admin_doctor.id)){
			setRightTitle("", null);
		}else if (!TextUtils.isEmpty(caseId) && UserContext.getInstance().isMyself(getCaseInfo().admin_doctor.id)){
			if(getCaseInfo().hasFlow()){
				setRightTitle("编辑", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CaseFlowEditActivity.show(getActivity(), caseId, flowInfo, requestType);
					}
				});
			}else{
				setRightTitle("", null);
			}
		}
//		if (requestType == QjConstant.REQUEST_CODE_NEW_CASE_FLOW) {
////			if (flowInfo == null || flowInfo.isEmpty()) {
////				CaseFlowSelectorActivity.show(CaseFlowDetailActivity.this);
////				return;
////			}
//		} else 
		if (requestType == QjConstant.REQUEST_CODE_CASE_FLOW_LIST
			|| requestType == QjConstant.REQUEST_CODE_NEW_CASE_FLOW) {
			View userbutton = findViewById(R.id.use_button);
			if(flowInfo!=null && !flowInfo.isEmpty()){
				userbutton.setVisibility(View.VISIBLE);
			}
			userbutton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
//					if (!TextUtils.isEmpty(caseId) && getCaseInfo() != null) {
//						CaseFlowEditActivity.show(getActivity(), caseId, flowInfo, requestType);
						Intent intent = new Intent();
						intent.setClass(getActivity(), CaseFlowEditActivity.class);
						intent.putExtra("requestType", requestType);
						intent.putExtra("from", "usebutton");
						setCaseId(intent, caseId);
						intent.putExtra("FlowInfo", (Parcelable) flowInfo);
						getActivity().startActivityForResult(intent, requestType);
//						onFinish();
						
						
//						HashMap<String, String> params = new HashMap<String, String>();
//						params.put("patient_id", caseId);
//						params.put("procedure_title", flowInfo.title);
//						params.put("procedure_text", flowInfo.procedure);
//						OkHttpUtils.post(QjHttp.URL_UPDATE_PATIENT, params, new BaseModelCallback() {
//
//							@Override
//							public void onError(Call call, Exception e) {
//								Toast.makeText(getActivity(), "保存失败",
//										Toast.LENGTH_LONG).show();
//							}
//
//							@Override
//							public void onResponseSucces(MBase response) {
//								Toast.makeText(getActivity(), "保存成功",
//										Toast.LENGTH_LONG).show();
//								CaseInfo tempCaseInfo = getCaseInfo();
//								if (tempCaseInfo != null) {
//									tempCaseInfo.procedure_title = flowInfo.title;
//									tempCaseInfo.procedure_text = flowInfo.procedure;
//								}
//								onFinish();
//							}
//
//						});
//					} else {
//						onFinish();
//					}
				}
			});
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case QjConstant.REQUEST_CODE_CASE_FLOW_LIST:
				findViewById(R.id.use_button).setVisibility(View.GONE);
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
				break;
			case QjConstant.REQUEST_CODE_NEW_CASE_FLOW:
				setResult(RESULT_OK, data);
//				FlowInfo flowInfo = data.getParcelableExtra("FlowInfo");
				if(data.getStringExtra("flag")!=null&&data.getStringExtra("flag").equals("delete")){
					finish();
				}
				flowInfo = data.getParcelableExtra("FlowInfo");
				onResult = true;
				bindView();
//				finish();
				break;
			case QjConstant.REQUEST_CODE_EDIT_CASE_FLOW:
				CaseInfo cas1 = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(caseId);
				if(cas1 == null) {
					finish();
					return;
				}
				String result1 = cas1.procedure_title;
				if(TextUtils.isEmpty(result1)) {
					finish();
				} else {
					bindView();
				}
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
			CaseFlowSelectorActivity.show(CaseFlowDetailActivity.this, caseId, requestType);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		bindView();
		if(getCaseInfo()==null){//新建
			if (requestType == QjConstant.REQUEST_CODE_NEW_CASE_FLOW ) {
				if (flowInfo != null && !flowInfo.isEmpty()
						&& (onResult||!(getIntent().getStringExtra("from")!=null&&getIntent().getStringExtra("from").equals("caseSelector")))){
					setRightTitle("编辑", new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							CaseFlowEditActivity.show(getActivity(), caseId, flowInfo, requestType);
						}
					});
				}else {
					setRightTitle("", null);
				}
			} 
		}else if(!UserContext.getInstance().isMyself(getCaseInfo().admin_doctor.id)){
			setRightTitle("", null);
		}else if (!TextUtils.isEmpty(caseId) && UserContext.getInstance().isMyself(getCaseInfo().admin_doctor.id)){
			if(getCaseInfo().hasFlow()){
				setRightTitle("编辑", new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						CaseFlowEditActivity.show(getActivity(), caseId, flowInfo, requestType);
					}
				});
			}else{
				setRightTitle("", null);
			}
		}

		 MobclickAgent.onResume(this);
	}
	

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 MobclickAgent.onPause(this);
	}

	@Override
	protected void initView() {
		super.initView();
		if(getCaseInfo()!=null && !UserContext.getInstance().isMyself(getCaseInfo().admin_doctor.id)){
			btn_add_flow_detail.setVisibility(View.GONE);
		}else {
			btn_add_flow_detail.setVisibility(View.VISIBLE);
		}
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
