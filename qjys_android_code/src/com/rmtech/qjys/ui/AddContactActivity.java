/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.rmtech.qjys.ui;

import okhttp3.Call;
import okhttp3.Response;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.hx.QjHelper;
import com.rmtech.qjys.model.MUser;
import com.rmtech.qjys.model.UserInfo;
import com.sjl.lib.http.okhttp.callback.Callback;

public class AddContactActivity extends BaseActivity {
	private EditText editText;
	private LinearLayout searchedUserLayout;
	private TextView nameText, mTextView;
	private Button searchBtn;
	private ImageView avatar;
	private InputMethodManager inputMethodManager;
	private String toAddUsername;
	private ProgressDialog progressDialog;
	private View search_resurt_tv;

	public static void show(Context context, String number) {
		Intent intent = new Intent();
		intent.setClass(context, AddContactActivity.class);
		intent.putExtra("phone_number", number);
		context.startActivity(intent);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_add_contact);
		mTextView = (TextView) findViewById(R.id.add_list_friends);
		search_resurt_tv = findViewById(R.id.no_data_view);

		editText = (EditText) findViewById(R.id.edit_note);
		String strAdd = getResources().getString(R.string.add_friend);
		mTextView.setText(strAdd);
		String strUserName = getResources().getString(R.string.user_name);
		editText.setHint(strUserName);
		searchedUserLayout = (LinearLayout) findViewById(R.id.ll_user);
		nameText = (TextView) findViewById(R.id.name);
		searchBtn = (Button) findViewById(R.id.search);
		avatar = (ImageView) findViewById(R.id.avatar);
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		parseIntent(getIntent());
	}

	private void parseIntent(Intent intent) {
		if (intent == null) {
			return;
		}
		String number = intent.getStringExtra("phone_number");
		if (!TextUtils.isEmpty(number)) {
			editText.setText(number);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		parseIntent(intent);
		super.onNewIntent(intent);
	}

	/**
	 * 查找contact
	 * 
	 * @param v
	 */
	public void searchContact(View v) {
		final String name = editText.getText().toString();
		String saveText = searchBtn.getText().toString();

		if (getString(R.string.button_search).equals(saveText)) {
			toAddUsername = name;
			if (TextUtils.isEmpty(name)) {
				new EaseAlertDialog(this, R.string.Please_enter_a_username).show();
				return;
			}
			// TODO 从服务器获取此contact,如果不存在提示不存在此用户
			QjHttp.serchContact(toAddUsername, new QjHttpCallback<MUser>() {

				@Override
				public MUser parseNetworkResponse(String response) throws Exception {
					MUser user = new Gson().fromJson(response, MUser.class);
					return user;

				}

				@Override
				public void onError(Call call, Exception e) {
					// TODO Auto-generated method stub
					onNoData();
				}

				@Override
				public void onResponseSucces(MUser response) {
					// 服务器存在此用户，显示此用户和添加按钮
					onHasData();
					if (response.data != null) {
						nameText.setTag(response.data);
						if (!TextUtils.isEmpty(response.data.name)) {
							nameText.setText(response.data.name);
						} else if (!TextUtils.isEmpty(response.data.phone)) {
							nameText.setText(response.data.phone);
						} else {
							onNoData();

						}

					} else {
						onNoData();
					}
				}
			});
		}
	}

	private void onNoData() {
		search_resurt_tv.setVisibility(View.VISIBLE);
		searchedUserLayout.setVisibility(View.GONE);
	}

	private void onHasData() {
		search_resurt_tv.setVisibility(View.GONE);
		searchedUserLayout.setVisibility(View.VISIBLE);
	}

	/**
	 * 添加contact
	 * 
	 * @param view
	 */
	public void addContact(View view) {
		final UserInfo info = (UserInfo) nameText.getTag();
		if(info == null || TextUtils.isEmpty(info.id)) {
			return;
		}
		if (EMClient.getInstance().getCurrentUser().equals(info.id)) {
			new EaseAlertDialog(this, R.string.not_add_myself).show();
			return;
		}

		if (QjHelper.getInstance().getContactList().containsKey(info.id)) {
			// 提示已在好友列表中(在黑名单列表里)，无需添加
			if (EMClient.getInstance().contactManager().getBlackListUsernames().contains(info.id)) {
				new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
				return;
			}
			new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
			return;
		}

		progressDialog = new ProgressDialog(this);
		String stri = getResources().getString(R.string.Is_sending_a_request);
		progressDialog.setMessage(stri);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();

		new Thread(new Runnable() {
			public void run() {

				try {
					// demo写死了个reason，实际应该让用户手动填入
					String s = getResources().getString(R.string.Add_a_friend);
					EMClient.getInstance().contactManager().addContact(info.id, s);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s1 = getResources().getString(R.string.send_successful);
							Toast.makeText(getApplicationContext(), s1, 1).show();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s2 = getResources().getString(R.string.Request_add_buddy_failure);
							Toast.makeText(getApplicationContext(), s2 + e.getMessage(), 1).show();
						}
					});
				}
			}
		}).start();
	}

	public void back(View v) {
		finish();
	}
}
