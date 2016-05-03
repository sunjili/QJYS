package com.rmtech.qjys.ui.qjactivity;

import android.os.Bundle;

import com.rmtech.qjys.QjConstant;

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
}
