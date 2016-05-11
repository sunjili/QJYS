package com.rmtech.qjys.ui.qjactivity;

import java.util.HashMap;

import okhttp3.Call;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.http.okhttp.OkHttpUtils;

/**
 * 性别 界面
 * 
 * @author Administrator
 * 
 */
public class MeSexActivity extends CaseEidtBaseActivity implements OnClickListener {
	private RelativeLayout ll_man;
	private RelativeLayout ll_woman;
	private ImageView iv_man;
	private ImageView iv_woman;
	private Context context;
	private int man = 0;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_sex);
		context = MeSexActivity.this;
		setTitle("性別");
		setRightTitle("保存", new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO 保存性别
				if(mCaseInfo != null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("sex", mCaseInfo.sex + "");
					params.put("patient_id", mCaseInfo.id);
					OkHttpUtils.post(QjHttp.URL_UPDATE_PATIENT, params, new BaseModelCallback() {

						@Override
						public void onError(Call call, Exception e) {
							Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_LONG).show();

						}

						@Override
						public void onResponseSucces(MBase response) {
							CaseInfo caseInfo = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(mCaseInfo.id);
							if(caseInfo != null) {
								caseInfo.sex = man;
							}
							Intent data = new Intent();
							data.putExtra("int", man);
							setResult(RESULT_OK, data);
							finish();
						}
					});
					return;
				}
				if (UserContext.getInstance().getUser().sex == man) {
					Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
					finish();
					return;
				}

				HashMap<String, String> params = new HashMap<String, String>();
				params.put("sex", man + "");
				QjHttp.updateUserinfo(params, new BaseModelCallback() {

					@Override
					public void onError(Call call, Exception e) {
						Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onResponseSucces(MBase response) {
						UserContext.getInstance().setUserSex(man);
						Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
						Intent data = new Intent();
						data.putExtra("int", man);
						setResult(MeNameActivity.RESULT_OK, data);
						finish();
					}

				});

			}
		});
		initView();
	}

	private void initView() {
		ll_man = (RelativeLayout) findViewById(R.id.ll_man);
		ll_man.setOnClickListener(this);
		ll_woman = (RelativeLayout) findViewById(R.id.ll_woman);
		ll_woman.setOnClickListener(this);
		iv_man = (ImageView) findViewById(R.id.iv_man);
		iv_woman = (ImageView) findViewById(R.id.iv_woman);
		int sex = UserContext.getInstance().getUser().sex;
		if(mCaseInfo != null) {
			sex = mCaseInfo.sex;
		}
		if (sex == 1) {
			man = 1;
			iv_man.setVisibility(View.VISIBLE);
			iv_woman.setVisibility(View.INVISIBLE);
		} else {
			man = 2;
			iv_woman.setVisibility(View.VISIBLE);
			iv_man.setVisibility(View.INVISIBLE);
		}

	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeSexActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_man:
			man = 1;
			iv_man.setVisibility(View.VISIBLE);
			iv_woman.setVisibility(View.INVISIBLE);

			break;
		case R.id.ll_woman:
			man = 2;
			iv_woman.setVisibility(View.VISIBLE);
			iv_man.setVisibility(View.INVISIBLE);

			break;
		default:
			break;
		}
	}
}
