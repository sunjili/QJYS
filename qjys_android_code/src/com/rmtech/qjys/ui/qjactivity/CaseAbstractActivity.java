package com.rmtech.qjys.ui.qjactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rmtech.qjys.R;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.GroupDetailsActivity;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.umeng.analytics.MobclickAgent;

/***
 * 摘要
 * 
 * @author Administrator
 * 
 */
public class CaseAbstractActivity extends CaseWithIdActivity {

	private TextView tv_content;
	private ImageView empty_img;
	private Button btn_add_abs_detail;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_case_abs_detail);
		setTitle("病例摘要");
		setLeftTitle("返回");
		initView();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
		 MobclickAgent.onResume(this);
		if (caseInfo != null) {
			CaseInfo tempCase = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(caseInfo.id);
			if (tempCase != null) {
				caseInfo = tempCase;
			}
		}
		setValue();
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 MobclickAgent.onPause(this);
	}

	private void setValue() {

		if (caseInfo != null && !TextUtils.isEmpty(caseInfo.abs)) {
			tv_content.setText(caseInfo.abs);
			tv_content.setVisibility(View.VISIBLE);
			empty_img.setVisibility(View.GONE);
			btn_add_abs_detail.setVisibility(View.GONE);
			setRightTitle("编辑", new OnClickListener() {

				@Override
				public void onClick(View v) {
					CaseAbstractEditActivity.show(getActivity(), caseInfo);
				}
			});
		} else if(caseInfo != null && TextUtils.isEmpty(caseInfo.abs)&&
				UserContext.getInstance().isMyself(caseInfo.admin_doctor.id)){
			tv_content.setVisibility(View.GONE);
			empty_img.setVisibility(View.VISIBLE);
			btn_add_abs_detail.setVisibility(View.VISIBLE);
			setRightTitle("", null);
		} else if(caseInfo != null && TextUtils.isEmpty(caseInfo.abs)&&
				!UserContext.getInstance().isMyself(caseInfo.admin_doctor.id)){
			tv_content.setVisibility(View.GONE);
			empty_img.setVisibility(View.VISIBLE);
			btn_add_abs_detail.setVisibility(View.GONE);
			setRightTitle("", null);
		}else if(caseInfo == null){
			tv_content.setVisibility(View.GONE);
			empty_img.setVisibility(View.VISIBLE);
			btn_add_abs_detail.setVisibility(View.GONE);
			setRightTitle("", null);
		}
	}

	private void initView() {

		tv_content = (TextView) findViewById(R.id.content_tv);
		btn_add_abs_detail = (Button) findViewById(R.id.btn_add_abs_detail);
		btn_add_abs_detail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 添加摘要
				CaseAbstractEditActivity.show(getActivity(), caseInfo);
			}
		});
		empty_img = (ImageView)findViewById(R.id.empty_img);

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
