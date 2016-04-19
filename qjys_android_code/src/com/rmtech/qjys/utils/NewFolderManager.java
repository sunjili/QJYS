package com.rmtech.qjys.utils;

import okhttp3.Call;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.model.FolderDataInfo;
import com.rmtech.qjys.model.gson.MFolderInfo;
import com.sjl.lib.alertview.AlertView;
import com.sjl.lib.alertview.OnDismissListener;
import com.sjl.lib.alertview.OnItemClickListener;

public class NewFolderManager {

	private InputMethodManager imm;
	private AlertView mAlertViewExt;
	private EditText etName;
	private Context context;

	public NewFolderManager(Context context) {
		this.context = context;
	}

	public interface OnNewFolderListener {
		void onAddNewFolder(FolderDataInfo data);
	}

	public void showNewFolderDialog(final String caseid, final String parentId, final OnNewFolderListener listener) {
		if (imm == null) {
			imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		}
		if (mAlertViewExt == null) {
			mAlertViewExt = new AlertView("新建文件夹", null, "取消", null, new String[] { "完成" }, context,
					AlertView.Style.Alert, new OnItemClickListener() {

						@Override
						public void onItemClick(Object o, int position) {
							closeKeyboard();
							if (o == mAlertViewExt && position != AlertView.CANCELPOSITION) {
								String name = etName.getText().toString();
								if (name.isEmpty()) {
									Toast.makeText(context, "请输入文件名", Toast.LENGTH_SHORT).show();
								} else {

									QjHttp.createFolder(caseid, name, parentId, new QjHttpCallback<MFolderInfo>() {

										@Override
										public void onError(Call call, Exception e) {
											// TODO Auto-generated method stub
											Toast.makeText(context, "创建文件夹失败！", Toast.LENGTH_SHORT).show();

										}

										@Override
										public void onResponseSucces(MFolderInfo response) {
											// TODO Auto-generated method stub
											if (listener != null) {
												listener.onAddNewFolder(response.data);
											}
										}

										@Override
										public MFolderInfo parseNetworkResponse(String str) throws Exception {
											// TODO Auto-generated method stub
											return new Gson().fromJson(str, MFolderInfo.class);
										}

									});
									
								}
								return;
							}
						}
					});
			ViewGroup extView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.alertext_form, null);
			etName = (EditText) extView.findViewById(R.id.etName);
			etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View view, boolean focus) {
					// 输入框出来则往上移动
					boolean isOpen = imm.isActive();
					mAlertViewExt.setMarginBottom(isOpen && focus ? 120 : 0);
				}
			});
			mAlertViewExt.addExtView(extView);
			mAlertViewExt.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss(Object o) {
					closeKeyboard();
				}
			});
		}
		mAlertViewExt.show();

	}

	private void closeKeyboard() {
		// 关闭软键盘
		if (imm != null) {
			imm.hideSoftInputFromWindow(etName.getWindowToken(), 0);
		}
		// 恢复位置
		mAlertViewExt.setMarginBottom(0);
	}

	public boolean isAlertShowing() {
		return (mAlertViewExt != null && mAlertViewExt.isShowing());
	}

	public void dismissAlert() {
		if (mAlertViewExt != null) {
			mAlertViewExt.dismiss();
		}

	}

}
