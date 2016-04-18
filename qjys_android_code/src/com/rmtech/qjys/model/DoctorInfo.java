package com.rmtech.qjys.model;

import java.io.Serializable;

public class DoctorInfo implements Serializable {

	/**
	 * 'id' : 医生id 'name' : 姓名 'head' : 头像 'phone' : 手机号
	 */
	private static final long serialVersionUID = 7865107684435794820L;

	public String id;
	public String name;
	public String head;
	public String idphone;
	public int sex;// : 性别
	public String hos_id;// : 医院id
	public String hos_fullname;// : 医院名称
	public String department;// : 科室

}
