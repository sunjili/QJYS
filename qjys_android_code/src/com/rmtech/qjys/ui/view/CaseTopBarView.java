package com.rmtech.qjys.ui.view;

import java.util.ArrayList;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.event.CaseEvent;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.ui.GroupDetailsActivity;
import com.rmtech.qjys.ui.qjactivity.CaseFlowDetailActivity;
import com.rmtech.qjys.ui.qjactivity.EditCaseActivity;
import com.rmtech.qjys.ui.qjactivity.MeAbstractActivity;
import com.rmtech.qjys.ui.qjactivity.MeFlowDetailActivity;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.sjl.lib.utils.L;

@SuppressLint("NewApi")
public class CaseTopBarView extends RelativeLayout implements
		View.OnClickListener {

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

	public CaseTopBarView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
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
		EventBus.getDefault().register(this);
	}

	@Subscribe
	public void onEvent(CaseEvent event) {
		if (event == null) {
			return;
		}
		if (caseInfo == null
				|| !TextUtils.equals(caseInfo.id, event.caseInfoId)) {
			return;
		}

		caseInfo = GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(
				caseInfo.id);
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		EventBus.getDefault().unregister(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.jb_layout:
			if (caseInfo == null) {
				Toast.makeText(getContext(), "病例已删除！", 1).show();

			} else {
				EditCaseActivity.show((Activity) getContext(), caseInfo);
			}
			break;
		case R.id.zy_layout:
			MeAbstractActivity.show(getActivity());
			break;
		case R.id.gf_layout:
			if (caseInfo == null) {
				Toast.makeText(getContext(), "病例已删除！", 1).show();

			} else {
				CaseFlowDetailActivity.show(getActivity(), caseInfo.id, null,
						QjConstant.REQUEST_CODE_EDIT_CASE_FLOW);

				// GroupDetailsActivity.show(getActivity(), caseInfo);
			}
			break;
		case R.id.yhz_layout:
			if (caseInfo == null) {
				Toast.makeText(getContext(), "病例已删除！", 1).show();

			} else {
				GroupDetailsActivity.show(getActivity(), caseInfo);
			}
			break;
		}

	}

	private Activity getActivity() {
		// TODO Auto-generated method stub
		return (Activity) getContext();
	}

	public void setCaseInfo(CaseInfo info) {
		this.caseInfo = info;
	}

}
