package com.rmtech.qjys.ui.qjactivity;

import android.content.Intent;
import android.os.Bundle;

import com.rmtech.qjys.ui.BaseActivity;

public class CaseWithIdActivity extends BaseActivity {
	protected String caseId;

	public static void setCaseId(Intent intent, String patient_id) {
		intent.putExtra("patient_id", patient_id);
	}

	@Override
	protected boolean showTitleBar() {
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null) {
			caseId = intent.getStringExtra("patient_id");
		}
	}

}
