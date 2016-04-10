package com.rmtech.qjys.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.rmtech.qjys.QjApplication;
import com.rmtech.qjys.QjHelper;
import com.rmtech.qjys.R;
import com.rmtech.qjys.db.DemoDBManager;
import com.rmtech.qjys.ui.MainActivity;

@SuppressLint("NewApi")
public class LoginPassWordView extends LoginBaseView implements View.OnClickListener {

	private static final String TAG = "LoginActivity";
	public static final int REQUEST_CODE_SETNICK = 1;
	private EditText usernameEditText;
	private EditText passwordEditText;

	private boolean progressShow;
	private boolean autoLogin = false;

	private String currentUsername;
	private String currentPassword;

	public LoginPassWordView(Context context) {
		this(context, null);
	}

	public LoginPassWordView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LoginPassWordView(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
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
		if (QjHelper.getInstance().getCurrentUsernName() != null) {
			usernameEditText.setText(QjHelper.getInstance().getCurrentUsernName());
		}
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

		progressShow = true;
		final ProgressDialog pd = new ProgressDialog(getContext());
		pd.setCanceledOnTouchOutside(false);
		pd.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				Log.d(TAG, "EMClient.getInstance().onCancel");
				progressShow = false;
			}
		});
		pd.setMessage(getResources().getString(R.string.Is_landing));
		pd.show();

		// After logout，the DemoDB may still be accessed due to async callback,
		// so the DemoDB will be re-opened again.
		// close it before login to make sure DemoDB not overlap
		DemoDBManager.getInstance().closeDB();

		// reset current user name before login
		QjHelper.getInstance().setCurrentUserName(currentUsername);

		final long start = System.currentTimeMillis();
		// 调用sdk登陆方法登陆聊天服务器
		Log.d(TAG, "EMClient.getInstance().login");
		EMClient.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

			@Override
			public void onSuccess() {
				Log.d(TAG, "login: onSuccess");

				if (!((Activity) getContext()).isFinishing() && pd.isShowing()) {
					pd.dismiss();
				}

				// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
				// ** manually load all local groups and
				EMClient.getInstance().groupManager().loadAllGroups();
				EMClient.getInstance().chatManager().loadAllConversations();

				// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
				boolean updatenick = EMClient.getInstance().updateCurrentUserNick(QjApplication.currentUserNick.trim());
				if (!updatenick) {
					Log.e("LoginActivity", "update current user nick fail");
				}
				// 异步获取当前用户的昵称和头像(从自己服务器获取，demo使用的一个第三方服务)
				QjHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();

				// 进入主页面
				Intent intent = new Intent(getContext(), MainActivity.class);
				((Activity) getContext()).startActivity(intent);

				((Activity) getContext()).finish();
			}

			@Override
			public void onProgress(int progress, String status) {
				Log.d(TAG, "login: onProgress");
			}

			@Override
			public void onError(final int code, final String message) {
				Log.d(TAG, "login: onError: " + code);
				if (!progressShow) {
					return;
				}
				((Activity) getContext()).runOnUiThread(new Runnable() {
					public void run() {
						pd.dismiss();
						Toast.makeText(getContext(), getResources().getString(R.string.Login_failed) + message,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
