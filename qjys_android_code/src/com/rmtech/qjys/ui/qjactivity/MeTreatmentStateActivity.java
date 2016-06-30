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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.CommonAdapter;
import com.rmtech.qjys.adapter.ViewHolder;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.callback.QjHttpCallbackNoParse;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MStateList;
import com.rmtech.qjys.model.gson.MStateList.StateInfo;
import com.rmtech.qjys.ui.BaseActivity;
import com.sjl.lib.alertview.AlertView;
import com.umeng.analytics.MobclickAgent;

/***
 * 病例就诊状态 页面
 * 
 * @author Administrator
 * 
 */
public class MeTreatmentStateActivity extends BaseActivity implements View.OnClickListener {
	private Button btn_state_add;
	private EditText et_state_add;
	private ListView lv_state;
	// private String[] userArray = new String[] { "门诊", "住院", "术后", "出院", "随诊",
	// "死亡", "转院", "术前", "重点关注" };

	private CommonAdapter mAdapter;
	private Context context;
	List<StateInfo> listems;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		context = MeTreatmentStateActivity.this;
		setContentView(R.layout.qj_me_treament_state);
//		setRightTitle("保存", new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				//
//				MeTreatmentStateActivity.this.finish();
//			}
//		});
		setTitle("添加就诊状态");
		setLeftTitle("我");
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadData();
		MobclickAgent.onResume(this);
	}

	private void initView() {
		btn_state_add = (Button) findViewById(R.id.btn_state_add);
		btn_state_add.setOnClickListener(this);
		et_state_add = (EditText) findViewById(R.id.et_state_add);
		getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		lv_state = (ListView) findViewById(R.id.lv_state);
	}

	private void loadData() {

		QjHttp.treateStateList(new QjHttpCallbackNoParse<MStateList>() {

			@Override
			public void onError(Call call, Exception e) {
				// TODO Auto-generated method stub
				// listems = new ArrayList<StateInfo>();
				// lv_state.setAdapter(mAdapter);

			}

			@Override
			public void onResponseSucces(boolean iscache, MStateList response) {
				// TODO Auto-generated method stub
				if (response.hasData()) {
					listems = new ArrayList<StateInfo>();
					// for (int i = 0; i < userArray.length; i++) {
					// listems.add(userArray[i]);
					// }
					listems.addAll(response.data);

					mAdapter = new CommonAdapter<StateInfo>(getApplicationContext(), listems,
							R.layout.qj_me_treament_state_item) {

						@Override
						public void convert(ViewHolder viewHolder, final StateInfo item) {
							viewHolder.setText(R.id.tv_name, item.name);
							viewHolder.getView(R.id.iv_delete).setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									new AlertView("确定删除？", null, "取消", new String[] { "确定" }, null, getActivity(),
											AlertView.Style.Alert, new com.sjl.lib.alertview.OnItemClickListener() {

												@Override
												public void onItemClick(Object o, int position) {
													if (position == 0) {
														QjHttp.deleteTreateState(item.id, new BaseModelCallback() {

															@Override
															public void onResponseSucces(MBase response) {
																Toast.makeText(getActivity(), "删除成功", Toast.LENGTH_LONG)
																		.show();
																listems.remove(item);
																mAdapter.notifyDataSetChanged();
															}

															@Override
															public void onError(Call call, Exception e) {
																Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_LONG)
																		.show();

															}
														});

													}

												}
											}).setCancelable(true).show();

								}
							});
						}

					};
					lv_state.setAdapter(mAdapter);
				}
			}
		});

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
			if (listems != null) {
				for (StateInfo info : listems) {
					if (TextUtils.equals(string, info.name)) {
						Toast.makeText(context, string + " 已存在！", Toast.LENGTH_LONG).show();
						return;
					}
				}
			}
			QjHttp.addTreateState(string, new BaseModelCallback() {

				@Override
				public void onResponseSucces(MBase response) {
					Toast.makeText(context, "保存成功！", Toast.LENGTH_LONG).show();
					loadData();
					et_state_add.setText("");
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			        if (imm != null) {  
			            imm.hideSoftInputFromWindow(et_state_add.getWindowToken(), 0);  
			        }  
					// if (!listems.contains(string)) {
					// listems.add(string);
					// mAdapter.notifyDataSetChanged();
					// et_state_add.setText("");
					// Toast.makeText(context, string + "添加成功",
					// Toast.LENGTH_LONG).show();
					// } else {
					// Toast.makeText(context, string + "状态已存在",
					// Toast.LENGTH_LONG).show();
					// }
				}

				@Override
				public void onError(Call call, Exception e) {
					Toast.makeText(context, "保存失败！", Toast.LENGTH_LONG).show();

				}
			});
			break;

		default:
			break;
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
