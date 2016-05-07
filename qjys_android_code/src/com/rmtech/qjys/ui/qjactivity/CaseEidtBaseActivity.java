package com.rmtech.qjys.ui.qjactivity;

import android.os.Bundle;

import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.ui.BaseActivity;

/***
 * 编辑页面
 * 
 * @author Administrator
 * 
 */

public class CaseEidtBaseActivity extends BaseActivity {

	protected CaseInfo mCaseInfo;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		mCaseInfo = getIntent().getParcelableExtra("CaseInfo");
	}
	
}
