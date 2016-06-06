package com.rmtech.qjys.ui.qjactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/***
 * 新建临床诊疗规范及流程 页面
 * 
 * @author Administrator
 * 
 */
public class MeFlowNewActivity extends MeFlowEditActivity {

	@Override
	protected void onCreate(Bundle arg0) {
		isNew = true;
		btn_delete.setVisibility(View.GONE);
		super.onCreate(arg0);
	}

	public static void show(Activity context, String caseId, int type) {
		Intent intent = new Intent();
		intent.setClass(context, MeFlowNewActivity.class);
		intent.putExtra("requestType", type);
		setCaseId(intent, caseId);
		context.startActivityForResult(intent,type);
	}

}
