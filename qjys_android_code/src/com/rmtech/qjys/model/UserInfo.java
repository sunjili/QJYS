package com.rmtech.qjys.model;

import java.io.Serializable;

public class UserInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8789584433095188767L;
	public String id;// : 用户id
	public String phone;// : 手机号
	public String head;// : 头像
	public String name;// : 姓名
	public int sex;// : 性别
	public String hxpasswd;// : 环信密码
	public String hos_id;// : 医院id
	public String department;// : 科室
	public AuthInfo authinfo;

	public static class AuthInfo {
		public String token;// ：加密串
		public int expire;// : 过期时间 秒
		public String uid;// : 用户id
	}

	public String getCookie() {
		if (authinfo != null) {
			StringBuilder cookie = new StringBuilder();
			cookie.append("token=" + authinfo.token + ";");
			cookie.append("expire=" + authinfo.expire + ";");
			cookie.append("uid=" + authinfo.uid);
			return cookie.toString();
		}
		return null;
	}

}
