package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.http.okhttp.OkHttpUtils;
import com.umeng.analytics.MobclickAgent;

/***
 * 编辑就诊 页面
 * 
 * @author Administrator
 * 
 */
public class CaseDiagnoseActivity extends CaseEidtBaseActivity implements View.OnClickListener {
	private LinearLayout ll_layout;
	private EditText et_diagnose;
	private LinearLayout ll_diagnose_add;
	private Context context;
	private String stringExtra;
	private String[] newStrings;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_case_diagnose);
		setTitle("诊断");
		setLeftTitle("返回");
		context = CaseDiagnoseActivity.this;
		setRightTitle("保存", new OnClickListener() {
			@Override
			public void onClick(View v) {
				stringExtra = "";

				if(mCaseInfo != null) {
					for(int i = 0 ; i< ll_layout.getChildCount();i++) {
						View view = ll_layout.getChildAt(i);
						TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
						String itemStr = tv_name.getText().toString();
						if(TextUtils.isEmpty(itemStr)) {
							continue;
						}
						if(!TextUtils.isEmpty(stringExtra)) {
							stringExtra = stringExtra + "&&"+ itemStr;//(strings[i]);
						} else {
							stringExtra = itemStr;
						}
					}
					String trim = et_diagnose.getText().toString().trim();
					if(!TextUtils.isEmpty(trim)) {
						if(!TextUtils.isEmpty(stringExtra)) {
							stringExtra = stringExtra + "&&"+ trim;//(strings[i]);
						} else {
							stringExtra = trim;
						}
					}

					HashMap<String, String> params = new HashMap<String, String>();
					params.put("diagnose", stringExtra);
					params.put("patient_id", mCaseInfo.id);
					OkHttpUtils.post(QjHttp.URL_UPDATE_PATIENT, params, new BaseModelCallback() {

						@Override
						public void onError(Call call, Exception e) {
							Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_LONG).show();

						}

						@Override
						public void onResponseSucces(MBase response) {
							Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();

							CaseInfo caseInfo = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(mCaseInfo.id);
							if (caseInfo != null) {
								caseInfo.diagnose = stringExtra;
							}
							Intent data = new Intent();
							data.putExtra("string", stringExtra);
							setResult(MeNameActivity.RESULT_OK, data);
							finish();
						}
					});

					return;
				
				}
				if (!TextUtils.isEmpty(stringExtra)) {
					Intent data = new Intent();
					data.putExtra("string", stringExtra);
					setResult(MeNameActivity.RESULT_OK, data);
				}
				finish();
			}
		});
		initView();
		if (mCaseInfo != null) {

			stringExtra = mCaseInfo.diagnose;
			if (!TextUtils.isEmpty(stringExtra)) {
				setViewValue(stringExtra);
			}
		}
	}

	private void setViewValue(String tempStr) {
		ll_layout.removeAllViews();
		if (TextUtils.isEmpty(tempStr)) {
		} else {
			String[] strings = tempStr.split("&&");
			final List<String> list = new ArrayList<String>();
			for (int i = 0; i < strings.length; i++) {
				if(TextUtils.isEmpty(strings[i])) {
					continue;
				}
				list.add(strings[i]);
				final int posi = i;
				View view = LayoutInflater.from(context).inflate(R.layout.qj_case_diagnose_item, null);
				TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
				ImageView btn_delete = (ImageView) view.findViewById(R.id.btn_delete);
				tv_name.setText(strings[i]);
				btn_delete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						list.remove(posi);
						ll_layout.removeViewAt(posi);
						String newString = "";
						for(String s : list){
							if(newString.equals("")){
								newString = s;
							}else{
								newString = newString + "&&" + s;
							}
						}
						stringExtra = newString;
					}
				});
				ll_layout.addView(view);
			}
		}
	}

	private void initView() {
		ll_layout = (LinearLayout) findViewById(R.id.ll_layout);
		et_diagnose = (EditText) findViewById(R.id.et_diagnose);
		setTextWhacher(CaseDiagnoseActivity.this, et_diagnose, 75);
		ll_diagnose_add = (LinearLayout) findViewById(R.id.ll_diagnose_add);
		ll_diagnose_add.setOnClickListener(this);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, CaseDiagnoseActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_diagnose_add:
            if(ll_layout.getChildCount()>8){
            	Toast.makeText(getActivity(), "您不能添加更多的诊断！！！", Toast.LENGTH_SHORT).show();
            	return;
            }
			String trim = et_diagnose.getText().toString().trim();
			if (!TextUtils.isEmpty(trim)) {
				if (!TextUtils.isEmpty(stringExtra)) {
					stringExtra = stringExtra + "&&" + trim;
				} else {
					stringExtra = trim;
				}
				setViewValue(stringExtra);
				et_diagnose.setText("");
			}
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
