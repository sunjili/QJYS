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
package com.rmtech.qjys.ui.qjactivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.AddContactActivity;
import com.rmtech.qjys.ui.BaseActivity;

public class QjAddContactActivity extends BaseActivity {

	private static final int REQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_qj_add_contact);
		setTitle("添加新朋友");
	}

	public void onSearchByPhoneClick(View view) {
		startActivity(new Intent(getActivity(), AddContactActivity.class));
	}

	protected BaseActivity getActivity() {
		return QjAddContactActivity.this;
	}

	public void onContactPickClick(View view) {
//		Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//		getActivity().startActivityForResult(intent, 1);
		
		Intent intent = new Intent(getActivity(), PhoneContactsActivity.class); 
		startActivityForResult(intent, REQUEST_CODE); 
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, QjAddContactActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE:
			if (resultCode == RESULT_OK) {
//				Uri contactData = data.getData();
//				Cursor cursor = managedQuery(contactData, null, null, null, null);
//				cursor.moveToFirst();
//				String num = this.getContactPhone(cursor);
//				AddContactActivity.show(getActivity(), num);
				
//				String phone = data.getStringExtra("phone"); 
//				phoneEditText.setText(phone); 
			}
			break;

		default:
			break;
		}
	}
	

	private String getContactPhone(Cursor cursor) {
		// TODO Auto-generated method stub
		int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
		int phoneNum = cursor.getInt(phoneColumn);
		String result = "";
		if (phoneNum > 0) {
			// 获得联系人的ID号
			int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);
			String contactId = cursor.getString(idColumn);
			// 获得联系人电话的cursor
			Cursor phone = null;
			try {
				phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
						ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + contactId, null, null);
				if (phone.moveToFirst()) {
					for (; !phone.isAfterLast(); phone.moveToNext()) {
						int index = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
						int typeindex = phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
						int phone_type = phone.getInt(typeindex);
						String phoneNumber = phone.getString(index);
						result = phoneNumber;
						// switch (phone_type) {//此处请看下方注释
						// case 2:
						// result = phoneNumber;
						// break;
						//
						// default:
						// break;
						// }
					}

				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (phone != null && !phone.isClosed()) {
					phone.close();
				}
			}
		}
		return result;
	}

}
