package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.CommonAdapter;
import com.rmtech.qjys.adapter.ViewHolder;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.UserInfo;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.fragment.MeFragment;
import com.rmtech.qjys.ui.view.MeItemLayout;
import com.sjl.lib.utils.L;

public class EditCaseStateActivity extends BaseActivity implements
		View.OnClickListener {
	private EditText et_state_add;
	private ListView lv_state;
	private String[] userArray = new String[] { "门诊", "住院", "术后", "出院", "随诊",
			"死亡", "转院", "术前", "重点关注" };

	private CommonAdapter mAdapter;
	private Context context;
	List<String> listems;
	private String state;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context = EditCaseStateActivity.this;
		setContentView(R.layout.qj_case_state);
		setRightTitle("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(!TextUtils.isEmpty(state)){
					Intent data = new Intent();
					data.putExtra("string", state);
					setResult(MeNameActivity.RESULT_OK, data);
				}
				finish();
			}
		});
		setTitle("设置就诊状态");
		initView();
	}

	private void initView() {
		et_state_add = (EditText) findViewById(R.id.et_state_add);
		et_state_add.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!TextUtils.isEmpty(s.toString())){
					state=s.toString();
					mAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		lv_state = (ListView) findViewById(R.id.lv_state);

		listems = new ArrayList<String>();
		for (int i = 0; i < userArray.length; i++) {
			listems.add(userArray[i]);
		}

		mAdapter = new CommonAdapter<String>(getApplicationContext(), listems,
				R.layout.qj_case_edit_state_item) {

			@Override
			public void convert(final ViewHolder viewHolder, final String item) {
				if(item.equals(state)){
					viewHolder.getView(R.id.iv_selcted).setVisibility(View.VISIBLE);
				}else{
					viewHolder.getView(R.id.iv_selcted).setVisibility(View.GONE);
				}
				viewHolder.setText(R.id.tv_name, item);
				viewHolder.getView(R.id.rl_item).setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								state=item;
								et_state_add.setText("");
								viewHolder.getView(R.id.iv_selcted).setVisibility(View.VISIBLE);
								mAdapter.notifyDataSetChanged();
							}
						});
			}

		};
		lv_state.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();

	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Activity context) {
		Intent intent = new Intent();
		intent.setClass(context, EditCaseStateActivity.class);
		context.startActivityForResult(intent, EditCaseActivity.REQUEST_CASE_STATE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_state_add:
			String string = et_state_add.getText().toString().trim();
			if (!TextUtils.isEmpty(string)) {
				if (!listems.contains(string)) {
					listems.add(string);
					mAdapter.notifyDataSetChanged();
					et_state_add.setText("");
					Toast.makeText(context, string + "添加成功", Toast.LENGTH_LONG)
							.show();
				} else {
					Toast.makeText(context, string + "状态已存在", Toast.LENGTH_LONG)
							.show();
				}
			}
			break;

		default:
			break;
		}
	}
}
