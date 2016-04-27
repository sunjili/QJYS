package com.rmtech.qjys.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentListener;
import com.hyphenate.easeui.ui.EaseChatFragment.GroupListener;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.rmtech.qjys.R;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.ui.qjactivity.PhotoDataManagerActivity;
import com.rmtech.qjys.ui.view.CaseTopBarView;
import com.rmtech.qjys.utils.GroupAndCaseListManager;

public class ChatGroupFragment extends ChatFragment implements
		EaseChatFragmentListener {
	private CaseTopBarView mCaseTopBarView;
	private CaseInfo mCaseInfo;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	protected void initView() {
		super.initView();
		mCaseTopBarView = (CaseTopBarView) getView().findViewById(
				R.id.topbar_view);
		mCaseTopBarView.setVisibility(View.VISIBLE);
		mCaseInfo = GroupAndCaseListManager.getInstance().getCaseInfoByGroupId(
				toChatUsername);

		mCaseTopBarView.setCaseInfo(mCaseInfo);
	}

	@Override
	protected void setUpView() {
		super.setUpView();
		if (mCaseInfo != null) {
			titleBar.setTitle(mCaseInfo.getShowName());
			titleBar.setRightLayoutClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					PhotoDataManagerActivity.show(getActivity(), mCaseInfo,
							null);
				}
			});
			titleBar.setRightText("影像资料");

		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

}
