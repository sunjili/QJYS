package com.rmtech.qjys.ui.fragment;

import android.content.DialogInterface;
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
import com.rmtech.qjys.ui.GroupDetailsActivity;
import com.rmtech.qjys.ui.qjactivity.PhotoDataManagerActivity;
import com.rmtech.qjys.ui.qjactivity.QjLoginActivity;
import com.rmtech.qjys.ui.view.CaseTopBarView;
import com.rmtech.qjys.ui.view.CustomSimpleDialog;
import com.rmtech.qjys.ui.view.CustomSimpleDialog.Builder;
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
		if(mCaseInfo == null) {
			getActivity().finish();
			return;
		}
        if(mCaseInfo.admin_doctor.isMyself() && 
        		(mCaseInfo.participate_doctor == null || mCaseInfo.participate_doctor.isEmpty())){
        	CustomSimpleDialog.Builder builder = new Builder(getActivity());  
            builder.setTitle("");  
            builder.setMessage("请先添加医护组成员后\n再进行群聊");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO Auto-generated method stub
    				dialog.dismiss();
    				getActivity().finish();
    			}

    		});  
            builder.setPositiveButton("去添加", new DialogInterface.OnClickListener() {
    			
    			@Override
    			public void onClick(DialogInterface dialog, int which) {
    				// TODO 添加医护组成员页面
    				GroupDetailsActivity.show(getActivity(), mCaseInfo);
    				getActivity().finish();

    			}

    		});
            builder.create().show();
        }
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
					if(getActivity() != null) {
						PhotoDataManagerActivity.show(getActivity(), mCaseInfo,
								null);
						getActivity().finish();
					}
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
