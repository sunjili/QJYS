package com.rmtech.qjys.ui.qjactivity;

import android.os.Bundle;

import com.rmtech.qjys.QjConstant;
import com.umeng.analytics.MobclickAgent;

/***
 * 
 * @author Administrator
 * 
 */
public class MeFlowBaseActivity extends CaseWithIdActivity {
	protected int requestType = QjConstant.REQUEST_CODE_ME_FLOW;

	@Override
	protected void onCreate(Bundle arg0) {
		requestType = getIntent().getIntExtra("requestType",
				QjConstant.REQUEST_CODE_ME_FLOW);
		super.onCreate(arg0);

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
