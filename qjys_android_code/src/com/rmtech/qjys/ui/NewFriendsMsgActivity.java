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

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.bumptech.glide.request.animation.GlideAnimation.ViewAdapter;
import com.rmtech.qjys.R;
import com.rmtech.qjys.adapter.NewFriendsMsgAdapter;
import com.rmtech.qjys.db.InviteMessgeDao;
import com.rmtech.qjys.domain.InviteMessage;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.ui.qjactivity.UserInfoActivity;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.DoctorListManager.OnGetDoctorInfoCallback;

/**
 * 申请与通知
 *
 */
public class NewFriendsMsgActivity extends BaseActivity implements OnItemClickListener{
	private ListView listView;

	List<InviteMessage> msgs = new ArrayList<InviteMessage>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_new_friends_msg);

		listView = (ListView) findViewById(R.id.list);
		listView.setClickable(true);
		listView.setOnItemClickListener(this);
		InviteMessgeDao dao = new InviteMessgeDao(this);
		msgs = dao.getMessagesList();
		//设置adapter
		NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs); 
		listView.setAdapter(adapter);
		dao.saveUnreadMessageCount(0);
		
	}

	public void back(View view) {
		finish();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
//		DoctorListManager.getInstance().getDoctorInfoByHXid(msgs.get(position).getFrom(), 
//				new OnGetDoctorInfoCallback() {
//			
//			@Override
//			public void onGet(DoctorInfo info) {
//				if(info != null) {
//					UserInfoActivity.show(getActivity(),info, "newFriendsMsg");
//				} 
//			}
//		});
	}
	
	
}
