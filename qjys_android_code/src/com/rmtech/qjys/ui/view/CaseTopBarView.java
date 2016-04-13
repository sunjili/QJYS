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
import com.rmtech.qjys.R;
import com.rmtech.qjys.db.DemoDBManager;
import com.rmtech.qjys.hx.QjHelper;
import com.rmtech.qjys.ui.MainActivity;

@SuppressLint("NewApi")
public class CaseTopBarView extends RelativeLayout implements View.OnClickListener {

	public CaseTopBarView(Context context) {
		this(context, null);
	}

	public CaseTopBarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CaseTopBarView(Context context, AttributeSet attrs, int defStyleAttr) {
		this(context, attrs, defStyleAttr, 0);
	}

	public CaseTopBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView();
	}

	private void initView() {
		View.inflate(getContext(), R.layout.bview_casetopbar, this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
