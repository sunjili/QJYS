package com.rmtech.qjys.ui.qjactivity;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import okhttp3.Call;

import org.greenrobot.eventbus.EventBus;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;

import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.event.CaseEvent;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MFlowList.FlowInfo;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.view.CustomSimpleDialog;
import com.rmtech.qjys.ui.view.CustomSimpleDialog.Builder;
import com.rmtech.qjys.utils.FlowListManager;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.http.okhttp.OkHttpUtils;
import com.umeng.analytics.MobclickAgent;

/***
 * 编辑临床诊疗规范及流程 页面
 * 
 * @author Administrator
 * 
 */
public class MeFlowEditActivity extends MeFlowBaseActivity implements
		View.OnClickListener {
	protected EditText et_title;
	protected EditText et_content;
	protected Button btn_delete;
	private String title;
	private String content;
	private Context context;
	public CheckBox iv_right;
	protected int requestCode;
	protected FlowInfo flowInfo;
	protected boolean isSave = false;
	protected boolean isNew = false;

	protected CaseInfo getCaseInfo() {
		return GroupAndCaseListManager.getInstance()
				.getCaseInfoByCaseId(caseId);

	}

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_flow_edit);
		setTitle("编辑临床诊疗规范及流程");
		context = MeFlowEditActivity.this;
		flowInfo = getIntent().getParcelableExtra("FlowInfo");
		setLeftTitle("返回");
		initView();
		setRightTitle("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
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
				try {
					if (procedure_text.getBytes("utf-8").length > 8000) {
						showDialog("你输入的字数已经超过了限制, 请删除部分内容！");
						return;
					}
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (requestType == QjConstant.REQUEST_CODE_EDIT_CASE_FLOW
						|| requestType == QjConstant.REQUEST_CODE_CASE_FLOW_LIST) {

					HashMap<String, String> params = new HashMap<String, String>();
					params.put("patient_id", caseId);
					params.put("procedure_title", procedure_title);
					params.put("procedure_text", procedure_text);
					OkHttpUtils.post(QjHttp.URL_UPDATE_PATIENT, params,
							new BaseModelCallback() {

								@Override
								public void onResponseSucces(MBase response) {
									// GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(caseId);
									CaseInfo newcase = getCaseInfo();
									if (newcase != null) {
										newcase.procedure_title = procedure_title;
										newcase.procedure_text = procedure_text;
										newcase = getCaseInfo();
//										CaseEvent caseEvent = new CaseEvent(
//												CaseEvent.TYPE_FLOW_CHANGED);
//										caseEvent.setCaseInfoId(caseId);
//										EventBus.getDefault().post(caseEvent);
										setResult(RESULT_OK, new Intent());
										MeFlowEditActivity.this.finish();
									}
									Toast.makeText(getActivity(), "保存成功", 1)
											.show();
								}

								@Override
								public void onError(Call call, Exception e) {
									Toast.makeText(getActivity(), "保存失败", 1)
											.show();
								}
							});

				} else {
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
					MeFlowEditActivity.this.finish();
				}
				if (requestType == QjConstant.REQUEST_CODE_ME_FLOW || isSave) {
					saveAsPlate();
				}
			}
		});
	}
	
	public void showDialog(String str){
		CustomSimpleDialog.Builder builder = new Builder(MeFlowEditActivity.this);  
        builder.setTitle("提示");  
        builder.setMessage(str);  
        builder.setPositiveButton("好的", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});  
        builder.create().show();
	}

	protected void initView() {
		et_title = (EditText) findViewById(R.id.et_title);
		setTextWhacher(MeFlowEditActivity.this, et_title, 75);
		et_content = (EditText) findViewById(R.id.et_content);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		btn_delete.setOnClickListener(this);
		iv_right = (CheckBox) findViewById(R.id.iv_right);
		// iv_right.setOnClickListener(this);
		iv_right.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				isSave = isChecked;
			}
		});
		switch (requestType) {
		case QjConstant.REQUEST_CODE_EDIT_CASE_FLOW:
			if (!TextUtils.isEmpty(caseId) && getCaseInfo() != null) {
				et_title.setText(getCaseInfo().procedure_title);
				et_content.setText(getCaseInfo().procedure_text);
			}
			break;
		case QjConstant.REQUEST_CODE_ME_FLOW:
			findViewById(R.id.bottom_layout).setVisibility(View.GONE);
			break;
		}
		if (flowInfo != null) {
			et_title.setText(flowInfo.title);
			et_content.setText(flowInfo.procedure);
		}

	}

	protected boolean showTitleBar() {
		return true;
	}

	protected void saveAsPlate() {
		FlowListManager.getInstance().addFlow(flowInfo,
				new BaseModelCallback() {

					@Override
					public void onResponseSucces(MBase response) {
						// Toast.makeText(getActivity(), "保存成功", 1).show();
						// setResult(RESULT_OK, new
						// Intent().putExtra("FlowInfo",
						// (Parcelable) flowInfo));
					}

					@Override
					public void onError(Call call, Exception e) {
						// TODO Auto-generated method stub
						// Toast.makeText(getActivity(), "保存失败", 1).show();
					}
				});
	}

	// public static void show(Activity activity, int requestCode) {
	// Intent intent = new Intent();
	// intent.setClass(activity, MeFlowEditActivity.class);
	// intent.putExtra("requestCode", requestCode);
	// activity.startActivityForResult(intent, requestCode);
	// }
	//
	// public static void show(Activity activity, String caseId, int
	// requestCode) {
	// Intent intent = new Intent();
	// intent.setClass(activity, MeFlowEditActivity.class);
	// intent.putExtra("requestCode", requestCode);
	// setCaseId(intent, caseId);
	// activity.startActivityForResult(intent, requestCode);
	// }

	public static void show(BaseActivity activity, FlowInfo item,
			int requestCode) {
		Intent intent = new Intent();
		intent.setClass(activity, MeFlowEditActivity.class);
		intent.putExtra("requestType", requestCode);
		intent.putExtra("FlowInfo", (Parcelable) item);
		activity.startActivityForResult(intent, requestCode);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_delete:
			QjHttp.deletetreateprocedure(flowInfo, new BaseModelCallback() {
				
				@Override
				public void onResponseSucces(MBase response) {
					// TODO Auto-generated method stub
					Toast.makeText(context, "删除成功!", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.putExtra("delete", "success");
					setResult(RESULT_OK, intent);
					getActivity().finish();
				}
				
				@Override
				public void onError(Call call, Exception e) {
					// TODO Auto-generated method stub
					
				}
			});
			break;
		case R.id.iv_right:
			Toast.makeText(context, "保存为模板", Toast.LENGTH_SHORT).show();
			iv_right.setChecked(true);
			break;

		default:
			break;
		}
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
