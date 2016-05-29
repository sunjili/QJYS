package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.CommonAdapter;
import com.rmtech.qjys.adapter.ViewHolder;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.model.gson.MFlowList;
import com.rmtech.qjys.model.gson.MFlowList.FlowInfo;

/***
 * 设置临床诊疗规范及流程 页面
 * 
 * @author Administrator
 * 
 */
public class MeFlowActivity extends MeFlowBaseActivity implements
		View.OnClickListener {
	private ImageView iv_empty_flow;
	private ListView lv_flow;
	private LinearLayout ll_flow_my;
	private Context context;
	private CommonAdapter<FlowInfo> mAdapter;
	private List<FlowInfo> listems;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_flow);
		setTitle("设置临床诊疗规范及流程");
		setLeftTitle("我");
		context = MeFlowActivity.this;
		setRightTitle(R.drawable.btn_case_new, new OnClickListener() {

			@Override
			public void onClick(View v) {
				MeFlowNewActivity.show(getActivity(), caseId, requestType);
			}
		});
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void onDisplayView() {
		if (null == listems || listems.size() == 0) {
			iv_empty_flow.setVisibility(View.VISIBLE);
			ll_flow_my.setVisibility(View.GONE);
		} else {
			iv_empty_flow.setVisibility(View.GONE);
			ll_flow_my.setVisibility(View.VISIBLE);
		}
	}

	private void initData() {
		QjHttp.treateProcedurelist(new QjHttpCallbackNoParse<MFlowList>() {

			@Override
			public void onError(Call call, Exception e) {
				onDisplayView();
			}

			@Override
			public void onResponseSucces(boolean iscache, MFlowList response) {
				if (response.hasData()) {
					listems = new ArrayList<FlowInfo>();
					listems.addAll(response.data);
					setValue();
				}
				onDisplayView();
			}
		});
	}

	private void setValue() {

		mAdapter = new CommonAdapter<FlowInfo>(getApplicationContext(),
				listems, R.layout.qj_meitem_view) {

			@Override
			public void convert(ViewHolder viewHolder, final FlowInfo item) {
				viewHolder.setText(R.id.tv_left, item.title);
				viewHolder.getView(R.id.rl_item).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								// Toast.makeText(context, item,
								// Toast.LENGTH_SHORT).show();
								// MeFlowEditActivity.show(getActivity(), item,
								// QjConstant.REQUEST_CODE_EDIT_FLOW);
								onFlowItemClick(item);
							}
						});
			}

		};
		lv_flow.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	protected void onFlowItemClick(final FlowInfo item) {
		MeFlowDetailActivity.show(getActivity(), item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case QjConstant.REQUEST_CODE_ME_FLOW:
				break;
			}
		}
	}

	private void initView() {
		ll_flow_my = (LinearLayout) findViewById(R.id.ll_flow_my);
		iv_empty_flow = (ImageView) findViewById(R.id.iv_empty_flow);
		lv_flow = (ListView) findViewById(R.id.lv_flow);
	}

	protected boolean showTitleBar() {
		return true;
	}

	@Override
	public void onClick(View v) {
	}
}
