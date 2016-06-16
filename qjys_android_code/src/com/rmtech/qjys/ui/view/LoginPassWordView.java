package com.rmtech.qjys.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import okhttp3.Call;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.rmtech.qjys.QjApplication;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.db.DemoDBManager;
import com.rmtech.qjys.hx.QjHelper;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.gson.MUser;
import com.rmtech.qjys.ui.MainActivity;
import com.rmtech.qjys.ui.WebViewActivity;
import com.rmtech.qjys.ui.qjactivity.QjLoginActivity;
import com.rmtech.qjys.ui.view.CustomSimpleDialog.Builder;
import com.umeng.analytics.MobclickAgent;

@SuppressLint("NewApi")
public class LoginPassWordView extends LoginBaseView implements View.OnClickListener {

	private EditText usernameEditText;
	private EditText passwordEditText;
	private String currentUsername;
	private String currentPassword;

	public LoginPassWordView(Context context) {
		this(context, null);
	}

	public LoginPassWordView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LoginPassWordView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public LoginPassWordView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView();
	}

	private void initView() {
		View.inflate(getContext(), R.layout.qj_pw_login_view, this);

		// 如果登录成功过，直接进入主页面
//		if (QjHelper.getInstance().isLoggedIn()) {
//			autoLogin = true;
//			((Activity) getContext()).startActivity(new Intent(getContext(), MainActivity.class));
//
//			return;
//		}

		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
        findViewById(R.id.fuwu).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getContext(), WebViewActivity.class);
				intent.putExtra("from", "xieyi");
				intent.putExtra("url", "http://m.qijiyisheng.com/user_agreement.html");
				getContext().startActivity(intent);
			}
		});
		// 如果用户名改变，清空密码
		usernameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordEditText.setText(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
	}

	/**
	 * 登录
	 * 
	 * @param view
	 */
	public boolean login(View view) {
		if (!EaseCommonUtils.isNetWorkConnected(getContext())) {
			Toast.makeText(getContext(), R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return false;
		}
		currentUsername = usernameEditText.getText().toString().trim();
		currentPassword = passwordEditText.getText().toString().trim();

		if (TextUtils.isEmpty(currentUsername)) {
			Toast.makeText(getContext(), R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(currentPassword)) {
			Toast.makeText(getContext(), R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return false;
		}
		
		logining = true;
		MobclickAgent.onEvent(getContext(), "action_login_by_password");
		QjHttp.pwLogin(currentUsername, currentPassword, mOnLoginListener);
		
		return true;
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
