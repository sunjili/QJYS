package com.rmtech.qjys.ui.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.rmtech.qjys.R;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.ui.qjactivity.EditCaseActivity;

@SuppressLint("NewApi")
public class CaseTopBarView extends RelativeLayout implements View.OnClickListener {

	public CaseTopBarView(Context context) {
		this(context, null);
	}

	public CaseTopBarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CaseTopBarView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public CaseTopBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView();
	}

	private LinearLayout jbLayout;
	private LinearLayout zyLayout;
	private LinearLayout gfLayout;
	private LinearLayout yhzLayout;
	private CaseInfo caseInfo;

	private void initView() {
		View.inflate(getContext(), R.layout.bview_casetopbar, this);

		jbLayout = (LinearLayout) findViewById(R.id.jb_layout);
		zyLayout = (LinearLayout) findViewById(R.id.zy_layout);
		gfLayout = (LinearLayout) findViewById(R.id.gf_layout);
		yhzLayout = (LinearLayout) findViewById(R.id.yhz_layout);
		jbLayout.setOnClickListener(this);
		zyLayout.setOnClickListener(this);
		gfLayout.setOnClickListener(this);
		yhzLayout.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.jb_layout:
			EditCaseActivity.show((Activity) getContext(),caseInfo);

			break;
		case R.id.zy_layout:
			break;
		case R.id.gf_layout:
			break;
		case R.id.yhz_layout:
			break;
		}

	}

	public void setCaseInfo(CaseInfo info) {
		this.caseInfo = info;
	}

}
