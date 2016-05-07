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
package com.rmtech.qjys;

import com.hyphenate.easeui.EaseConstant;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class QjConstant extends EaseConstant {
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	public static final String GROUP_USERNAME = "item_groups";
	public static final String CHAT_ROOM = "item_chatroom";
	public static final String ACCOUNT_REMOVED = "account_removed";
	public static final String ACCOUNT_CONFLICT = "conflict";
	public static final String CHAT_ROBOT = "item_robots";
	public static final String MESSAGE_ATTR_ROBOT_MSGTYPE = "msgtype";
	public static final String ACTION_GROUP_CHANAGED = "action_group_changed";
	public static final String ACTION_CONTACT_CHANAGED = "action_contact_changed";

	public static final int REQUEST_IMAGE = 2;
	public static final int REQUEST_CAMERA = 100;
	public static final int REQUEST_CODE = 6384;
	public static final int REQUEST_CODE_ADD_DOCTORS = 4003;
	public static final int REQUEST_CODE_DELETE_DOCTORS = 4004;
	public static final int REQUEST_CODE_CHANGE_DOCTOR = 4005;
	public static final int REQUEST_CODE_NEW_CASE = 4006;
	public static final int REQUEST_CODE_ADD_HOSPITAL = 4;
	public static final int REQUEST_CODE_NEW_CASE_DOCTOR = 4010;
	
	public static final int REQUEST_CODE_EDIT_CASE_FLOW = 4009;
	public static final int REQUEST_CODE_NEW_CASE_FLOW = 4011;
	public static final int REQUEST_CODE_ME_FLOW = 4012; 
	public static final int REQUEST_CODE_CASE_FLOW_LIST = 4013; //从新建和编辑病例进 模版选择页
	public static final int REQUEST_CODE_EDIT_CASE = 4014;
//	public static final int REQUEST_CODE_EDIT_ME_FLOW = 4014; //从详情页进编辑页

	public static final DisplayImageOptions optionsImage = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.default_error)
			.showImageOnFail(R.drawable.default_error)
			.showImageOnLoading(R.drawable.default_error)
			.resetViewBeforeLoading(true).cacheOnDisk(true).cacheInMemory(true)
			.build();
	public static final DisplayImageOptions optionsHead = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.ic_default_avatar)
			.showImageOnFail(R.drawable.ic_default_avatar)
			.showImageOnLoading(R.drawable.ic_default_avatar)
			.resetViewBeforeLoading(true).cacheOnDisk(true).cacheInMemory(true)
			.build();

}
