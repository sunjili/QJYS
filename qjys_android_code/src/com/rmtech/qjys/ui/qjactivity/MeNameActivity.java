package com.rmtech.qjys.ui.qjactivity;

import java.util.HashMap;

import okhttp3.Call;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.ui.BaseActivity;

/***
 * 编辑姓名 页面
 * 
 * @author Administrator
 * 
 */
public class MeNameActivity extends BaseActivity {
	/** 姓名 */
	private EditText et_name;
	private String name = "";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_name);
		setTitle("姓名");
		setRightTitle("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				name = et_name.getText().toString().trim();
				if (TextUtils.equals(UserContext.getInstance().getUser().name, name)) {
					Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
					finish();
					return;
				}
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("name", name);
				QjHttp.updateUserinfo(params, new BaseModelCallback() {

					@Override
					public void onError(Call call, Exception e) {
						Toast.makeText(getActivity(), "保存失败", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onResponseSucces(MBase response) {
						UserContext.getInstance().setUserName(name);
						Toast.makeText(getActivity(), "保存成功", Toast.LENGTH_LONG).show();
						Intent data = new Intent();
						data.putExtra("string", name);
						setResult(MeNameActivity.RESULT_OK, data);
						finish();
					}

				});

			}
		});
		name = UserContext.getInstance().getUserName();
		initView();
	}

	private void initView() {
		et_name = (EditText) findViewById(R.id.et_name);
		et_name.setText(name);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context, String name) {
		Intent intent = new Intent();
		intent.setClass(context, MeNameActivity.class);
		context.startActivity(intent);
	}
}
