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
package com.hyphenate.easeui.domain;

import android.text.TextUtils;

import com.hyphenate.chat.EMContact;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.DoctorListManager.OnGetDoctorInfoCallback;

public class EaseUser extends EMContact {

	/**
	 * 昵称首字母
	 */
	protected String initialLetter;
	/**
	 * 用户头像
	 */
	protected String avatar;

	public DoctorInfo doctorInfo;

	public EaseUser(String username) {
		this.username = username;
		DoctorListManager.getInstance().getDoctorInfoByHXid(username, new OnGetDoctorInfoCallback() {
			
			@Override
			public void onGet(DoctorInfo info) {
				doctorInfo = info;
				if(TextUtils.isEmpty(initialLetter)) {
					EaseCommonUtils.setUserInitialLetter(EaseUser.this);
				}
			}
		});
		
	}

	public String getInitialLetter() {
		return initialLetter;
	}

	@Override
	public String getNick() {
		if (doctorInfo != null) {
			if (!TextUtils.isEmpty(doctorInfo.getDisplayName())) {
				return doctorInfo.getDisplayName();

			} else if (!TextUtils.isEmpty(doctorInfo.phone)) {
				return doctorInfo.phone;
			}
		}
		return super.getNick();
	}

	public void setInitialLetter(String initialLetter) {
		this.initialLetter = initialLetter;
	}

	public String getAvatar() {
		if (doctorInfo != null) {
			if (!TextUtils.isEmpty(doctorInfo.head)) {
				return doctorInfo.head;
			}
		}
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public int hashCode() {
		return 17 * getUsername().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof EaseUser)) {
			return false;
		}
		return getUsername().equals(((EaseUser) o).getUsername());
	}

	@Override
	public String toString() {
		return nick == null ? username : nick;
	}
}
