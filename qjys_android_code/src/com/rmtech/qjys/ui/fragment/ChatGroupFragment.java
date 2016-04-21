package com.rmtech.qjys.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentListener;
import com.rmtech.qjys.R;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.ui.view.CaseTopBarView;
import com.rmtech.qjys.utils.GroupAndCaseListManager;

public class ChatGroupFragment extends ChatFragment implements EaseChatFragmentListener {
	private CaseTopBarView mCaseTopBarView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	

	@Override
	protected void initView() {
		super.initView();
		mCaseTopBarView = (CaseTopBarView)getView().findViewById(R.id.topbar_view);
		mCaseTopBarView.setVisibility(View.VISIBLE);
		CaseInfo info = GroupAndCaseListManager.getInstance().getCaseInfoByGroupId(toChatUsername);
				
		mCaseTopBarView.setCaseInfo(info);
	}


	@Override
	protected void setUpView() {
		super.setUpView();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

}
