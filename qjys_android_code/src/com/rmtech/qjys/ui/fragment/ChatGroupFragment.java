package com.rmtech.qjys.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import okhttp3.Call;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.hyphenate.easeui.ui.EaseChatFragment.EaseChatFragmentListener;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.gson.MGroupList;
import com.rmtech.qjys.ui.GroupDetailsActivity;
import com.rmtech.qjys.ui.qjactivity.PhotoDataManagerActivity;
import com.rmtech.qjys.ui.view.CaseTopBarView;
import com.rmtech.qjys.ui.view.CustomSimpleDialog;
import com.rmtech.qjys.ui.view.CustomSimpleDialog.Builder;
import com.rmtech.qjys.utils.GroupAndCaseListManager;
import com.rmtech.qjys.utils.GroupAndCaseListManager.OnGetCaseInfoCallback;

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

		titleBar.setLeftTitle("返回");
		titleBar.setRightText("影像资料");
		if(mCaseInfo == null) {
			QjHttp.getGroupinfo(false, toChatUsername, new QjHttpCallbackNoParse<MGroupList>() {

				@Override
				public MGroupList parseNetworkResponse(String str) throws Exception {
					return new Gson().fromJson(str, MGroupList.class);
				}

				@Override
				public void onResponseSucces(boolean isCache, MGroupList response) {
					if (response != null && response.data != null && !response.data.isEmpty()) {
						mCaseInfo = response.data.get(0);
					}
					bindView();
				}

				@Override
				public void onError(Call call, Exception e) {
					getActivity().finish();
					return;
				}
			});
		}else {
			GroupAndCaseListManager.getInstance().getCaseInfoByCaseId(mCaseInfo.id, new OnGetCaseInfoCallback() {
				
				@Override
				public void onGet(CaseInfo info) {
					// TODO Auto-generated method stub
					if(info != null) {
						mCaseInfo = info;
					}
					bindView();
				}
			});
		}
       
	}

	private void bindView() {
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
			
		if (mCaseInfo != null) {
			titleBar.setTitle(mCaseInfo.name);
			titleBar.setRightLayoutClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if(getActivity() != null) {
						PhotoDataManagerActivity.show(getActivity(), mCaseInfo,
								null);
						getActivity().overridePendingTransition(R.anim.hold, R.anim.hold); 
						getActivity().finish();
					}
				}
			});
		}
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
