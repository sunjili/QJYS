package com.rmtech.qjys.ui.view;

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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.LoginActivity;

@SuppressLint("NewApi")
public class LoginVcodeView extends RelativeLayout implements View.OnClickListener {

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
		findViewById(R.id.login_button).setOnClickListener(this);
		code_button = (Button) findViewById(R.id.code_button);
		code_button.setOnClickListener(this);
		phone_edit = (EditText) findViewById(R.id.phone_edit);
		code_edit = (EditText) findViewById(R.id.code_edit);
		isGettingCode = false;
		mHandler = new Handler();
	}

	public static void show(FragmentActivity activity) {
		Intent intent = new Intent();
		intent.setClass(activity, LoginActivity.class);
		activity.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		String inuptPhoneStr = phone_edit.getEditableText().toString();
		switch (v.getId()) {
		case R.id.login_button: {
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
			break;
		}
		case R.id.code_button: {
			int errorCode = checkInput(inuptPhoneStr);
			if (errorCode != 0) {
				Toast.makeText(getContext(), "用户名请输入11位手机号码！", Toast.LENGTH_LONG).show();
				return;
			}
			if (isGettingCode) {
				return;
			}
			isGettingCode = true;
			// Applycode loader = new Applycode(inuptPhoneStr);
			// loader.reload();
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

	boolean logining = false;

	private void httpLogin(String inuptPhoneStr, String codeStr) {
		logining = true;
		// UserLogin.get(this, inuptPhoneStr, codeStr, new
		// LoaderListener<User>() {
		//
		// @Override
		// public void onLoadEnd(int pageflag, User obj) {
		// logining = false;
		// Toast.makeText(LoginActivity.this, "登录成功！",
		// Toast.LENGTH_LONG).show();
		// backToHome();
		// }
		//
		// @Override
		// public void onLoadError(int pageflag, int errCode, String errMsg) {
		// // TODO Auto-generated method stub
		// Toast.makeText(LoginActivity.this, "登录失败！＝" + errMsg,
		// Toast.LENGTH_LONG).show();
		// logining = false;
		// }
		//
		// });
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
