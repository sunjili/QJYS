package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.CommonAdapter;
import com.rmtech.qjys.adapter.ViewHolder;
import com.rmtech.qjys.ui.BaseActivity;

/***
 * 病例就诊状态 页面
 * 
 * @author Administrator
 * 
 */
public class MeTreatmentStateActivity extends BaseActivity implements View.OnClickListener{
	private Button btn_state_add;
	private EditText et_state_add;
	private ListView lv_state;
	private String[]  userArray=new String[]{"门诊","住院","术后","出院","随诊","死亡","转院","术前","重点关注"};
	
	private CommonAdapter mAdapter;
	private Context context;
	List<String> listems;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context = MeTreatmentStateActivity.this;
		setContentView(R.layout.qj_me_treament_state);
		setRightTitle("保存", new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//
				MeTreatmentStateActivity.this.finish();
			}
		});
		setTitle("设置就诊状态");
		initView();
	}


	private void initView() {
		btn_state_add = (Button) findViewById(R.id.btn_state_add);
		btn_state_add.setOnClickListener(this);
		et_state_add = (EditText) findViewById(R.id.et_state_add);
		lv_state = (ListView) findViewById(R.id.lv_state);
		
		listems=new ArrayList<String>();
		for (int i = 0; i < userArray.length; i++) {
			listems.add(userArray[i]);
		}
		
		mAdapter = new CommonAdapter<String>(getApplicationContext(),
				listems, R.layout.qj_me_treament_state_item)
		{
			
			@Override
			public void convert(ViewHolder viewHolder,  final String item)
			{
				viewHolder.setText(R.id.tv_name, item);
				viewHolder.getView(R.id.iv_delete).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						for (int i = 0; i < userArray.length; i++) {
							
						}
						listems.remove(item);
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

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeTreatmentStateActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_state_add:
			String string = et_state_add.getText().toString().trim();
			if(!TextUtils.isEmpty(string)){
				if(!listems.contains(string)){
					listems.add(string);
					mAdapter.notifyDataSetChanged();
					et_state_add.setText("");
					Toast.makeText(context, string+"添加成功", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(context, string+"状态已存在", Toast.LENGTH_LONG).show();
				}
			}
			break;

		default:
			break;
		}
	}
}
