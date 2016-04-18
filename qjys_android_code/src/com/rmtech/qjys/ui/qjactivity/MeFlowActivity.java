package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.CommonAdapter;
import com.rmtech.qjys.adapter.ViewHolder;
import com.rmtech.qjys.ui.BaseActivity;

/***
 * 设置临床诊疗规范及流程 页面
 * 
 * @author Administrator
 * 
 */
public class MeFlowActivity extends BaseActivity implements
		View.OnClickListener {
	private ImageView iv_empty_flow;
	private ListView lv_flow;
	private LinearLayout ll_flow_my;
	private Context context;
	private CommonAdapter<String> mAdapter;
	private List<String> listems;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_flow);
		setTitle("设置临床诊疗规范及流程");
		context = MeFlowActivity.this;
		setRightTitle("添加", new OnClickListener() {

			@Override
			public void onClick(View v) {
				MeFlowEditActivity.show(context);
			}
		});
		initView();
		initData();
		if (null == listems || listems.size() == 0) {
			iv_empty_flow.setVisibility(View.VISIBLE);
			ll_flow_my.setVisibility(View.GONE);
		} else {
			iv_empty_flow.setVisibility(View.GONE);
			ll_flow_my.setVisibility(View.VISIBLE);
		}
		setValue();
	}

	private void initData() {
		listems = new ArrayList<String>();
		listems.add("1头盖骨诊疗规范");
		listems.add("2头盖骨诊疗规范");
		listems.add("3头盖骨诊疗规范");
	}

	private void setValue() {

		mAdapter = new CommonAdapter<String>(getApplicationContext(), listems,
				R.layout.qj_meitem_view) {

			@Override
			public void convert(ViewHolder viewHolder, final String item) {
				viewHolder.setText(R.id.tv_left, item);
				viewHolder.getView(R.id.rl_item).setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								Toast.makeText(context, item,
										Toast.LENGTH_SHORT).show();
								MeFlowDetailActivity.show(context);
								mAdapter.notifyDataSetChanged();
							}
						});
			}

		};
		lv_flow.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();
	}

	private void initView() {
		ll_flow_my = (LinearLayout) findViewById(R.id.ll_flow_my);
		iv_empty_flow = (ImageView) findViewById(R.id.iv_empty_flow);
		lv_flow = (ListView) findViewById(R.id.lv_flow);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeFlowActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
	}
}
