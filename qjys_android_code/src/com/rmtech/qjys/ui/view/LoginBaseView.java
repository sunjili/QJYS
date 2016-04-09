package com.rmtech.qjys.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.rmtech.qjys.ui.callback.OnLoginListener;

public class LoginBaseView extends RelativeLayout {
	
	public boolean logining = false;


	public LoginBaseView(Context context) {
		this(context, null);
	}

	public LoginBaseView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LoginBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@SuppressLint("NewApi")
	public LoginBaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
	}

	protected OnLoginListener mOnLoginListener;

	public void setOnLoginListener(OnLoginListener listener) {
		mOnLoginListener = listener;
	}
	
	/**
	 * 登录
	 * 
	 * @param view
	 */
	public void login(View view) {
		
	}

}