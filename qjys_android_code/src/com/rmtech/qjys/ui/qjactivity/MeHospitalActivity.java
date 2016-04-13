package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.CommonAdapter;
import com.rmtech.qjys.adapter.ViewHolder;
import com.rmtech.qjys.ui.BaseActivity;

/***
 * 医院 页面
 * 
 * @author Administrator
 * 
 */
public class MeHospitalActivity extends BaseActivity {
	/** 医院名称 */
	private EditText et_hospital;

	private ListView lv_hospital;
	private CommonAdapter mAdapter;
	private Context context;
	List<String> listems;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context = MeHospitalActivity.this;
		setContentView(R.layout.qj_me_hospital);
		setRightTitle("保存", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MeHospitalActivity.this.finish();
			}
		});
		setTitle("医院");
		initView();
		setValue();
	}

	private void setValue() {
		listems = new ArrayList<String>();
		

	}

	private void initView() {
		lv_hospital = (ListView) findViewById(R.id.lv_hospital);
		et_hospital = (EditText) findViewById(R.id.et_hospital);
		et_hospital.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(final CharSequence s, int start, int before,
					int count) {
				if(s.length()==0){
					lv_hospital.setVisibility(View.INVISIBLE);
				}else{
					lv_hospital.setVisibility(View.VISIBLE);
				}
				listems.clear();
				for (int i = 0; i < s.length(); i++) {
					listems.add("测试" + i);
				}
				mAdapter = new CommonAdapter<String>(getApplicationContext(),
						listems, R.layout.qj_me_hospital_item)
				{
					@Override
					public void convert(ViewHolder viewHolder, final String item)
					{
						viewHolder.setTextAndColor(R.id.tv_name, item,s.toString(),Color.rgb(52, 100, 169));
						viewHolder.getView(R.id.rl_hospital_item).setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) { 
								Toast.makeText(context, item, Toast.LENGTH_SHORT).show();
								et_hospital.setText(item);
							}
						});
					}
				};
				lv_hospital.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
			
				
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
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeHospitalActivity.class);
		context.startActivity(intent);
	}
}
