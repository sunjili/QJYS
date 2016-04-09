package com.rmtech.qjys.ui.view;

import okhttp3.Call;
import okhttp3.Response;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.model.MBase;
import com.rmtech.qjys.model.MBase.BaseCallback;
import com.rmtech.qjys.ui.LoginActivity;
import com.sjl.lib.http.okhttp.callback.Callback;

@SuppressLint("NewApi")
public class LoginVcodeView extends LoginBaseView implements View.OnClickListener {

	private boolean isGettingCode;

	private Handler mHandler;

	private EditText code_edit;

	private Button code_button;

	private EditText phone_edit;

	public LoginVcodeView(Context context) {
		this(context, null);
	}

	public LoginVcodeView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LoginVcodeView(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	public LoginVcodeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView();
	}

	private void initView() {
		View.inflate(getContext(), R.layout.qj_vcode_login_view, this);
		code_button = (Button) findViewById(R.id.code_button);
		code_button.setOnClickListener(this);
		phone_edit = (EditText) findViewById(R.id.phone_edit);
		code_edit = (EditText) findViewById(R.id.code_edit);
		// findViewById(R.id.change_button).setOnClickListener(this);
		isGettingCode = false;
		mHandler = new Handler();
	}

	public static void show(FragmentActivity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, LoginActivity.class);
		activity.startActivity(intent);
	}

	@Override
	public void login(View view) {
		String inuptPhoneStr = phone_edit.getEditableText().toString();
		int errorCode = checkInput(inuptPhoneStr);
		if (errorCode != 0) {
			Toast.makeText(getContext(), "用户名请输入11位手机号码！", Toast.LENGTH_LONG).show();
			return;
		}
		String codeStr = code_edit.getEditableText().toString();
		if (TextUtils.isEmpty(codeStr)) {
			Toast.makeText(getContext(), "请输入验证码！", Toast.LENGTH_SHORT).show();
			return;
		}
		httpLogin(inuptPhoneStr, codeStr);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.code_button: {
			String inuptPhoneStr = phone_edit.getEditableText().toString();

			int errorCode = checkInput(inuptPhoneStr);
			if (errorCode != 0) {
				Toast.makeText(getContext(), "用户名请输入11位手机号码！", Toast.LENGTH_LONG).show();
				return;
			}
			if (isGettingCode) {
				return;
			}
			isGettingCode = true;
			QjHttp.getVcode(inuptPhoneStr, new BaseCallback() {
				
				@Override
				public void onResponse(MBase response) {
					if(response == null || response.ret != 0) {
						Toast.makeText(getContext(), "获取验证码失败", Toast.LENGTH_LONG).show();
					}
					
				}
				
				@Override
				public void onError(Call call, Exception e) {
					Toast.makeText(getContext(), "获取验证码失败", Toast.LENGTH_LONG).show();
				}
			});
			mHandler.postDelayed(mTimerRunnable, 1000);
			break;
		}
		}
	}

	private int mSecond = 60;
	private Runnable mTimerRunnable = new Runnable() {

		@Override
		public void run() {
			if (mSecond > 0) {
				mSecond--;
				code_button.setEnabled(false);
				code_button.setText(mSecond + "s");
				mHandler.postDelayed(mTimerRunnable, 1000);
			} else {
				isGettingCode = false;
				mSecond = 60;
				code_button.setEnabled(true);
				code_button.setText("获取验证码");
				code_button.setTextColor(getResources().getColor(R.color.white));
				// code_button.setBackgroundResource(R.drawable.btn_bg1_selector);
			}
		}
	};


	private void httpLogin(String inuptPhoneStr, String codeStr) {
		logining = true;
		QjHttp.login(inuptPhoneStr, codeStr, mOnLoginListener);
	}

	private void backToHome() {
		// setResult(RESULT_OK);
		// finish();
	}

	private int checkInput(String inputStr) {
		if (!TextUtils.isEmpty(inputStr) && inputStr.length() == 11) {
			for (int i = 0; i < inputStr.length(); i++) {
				char c = inputStr.charAt(i);
				if (!Character.isDigit(c)) {
					return 1;
				}
			}
			return 0;
		}
		return 1;
	}

}
