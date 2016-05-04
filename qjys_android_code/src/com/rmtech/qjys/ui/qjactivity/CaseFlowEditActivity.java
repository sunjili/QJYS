package com.rmtech.qjys.ui.qjactivity;

import java.util.HashMap;

import org.greenrobot.eventbus.EventBus;

import okhttp3.Call;
import android.app.Activity;
import android.app.usage.UsageEvents.Event;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.rmtech.qjys.utils.FlowListManager;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.http.okhttp.OkHttpUtils;

/***
 * 编辑临床诊疗规范及流程 页面
 * 
 * @author Administrator
 * 
 */
public class CaseFlowEditActivity extends MeFlowEditActivity implements
		View.OnClickListener {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
	}

	@Override
	protected void initView() {
		super.initView();
		btn_delete.setVisibility(View.GONE);

	}

	public static void show(BaseActivity activity, String caseId,
			FlowInfo item, int requestCode) {
		Intent intent = new Intent();
		intent.setClass(activity, CaseFlowEditActivity.class);
		intent.putExtra("requestType", requestCode);
		setCaseId(intent, caseId);
		intent.putExtra("FlowInfo", (Parcelable) item);
		activity.startActivityForResult(intent, requestCode);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_delete:
			Toast.makeText(getActivity(), "删除此规范", Toast.LENGTH_SHORT).show();
			break;
		case R.id.iv_right:
			Toast.makeText(getActivity(), "保存为模板", Toast.LENGTH_SHORT).show();
			break;

		default:
			break;
		}
	}

}
