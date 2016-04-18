package com.rmtech.qjys.model;

import java.io.Serializable;
import java.util.List;

public class CaseInfo implements Serializable {

	/**
	 * name: 姓名 hos_id：医院id sex ：性别 0未知 1男 2女 age : 年龄 hos_id : 医院id department：
	 * 科室 ward_no : 病房号 bed_no : 床位号 diagnose : 诊断结果，如果是多个诊断结果，用&&分割，给客户端显示自行分割
	 * treat_state ： 就诊状态 procedure_title ： 诊疗规范标题 procedure_text ： 诊疗规范内容
	 * abstract ： 摘要 state : 0正常 1临时 返回结构 { ret: 0 msg: 成功 data: { id ： 病例id } }
	 */
	private static final long serialVersionUID = -7846017874522166789L;

	public String id;// : 病例id
	public String name;// : 姓名
	public String hos_id;// ：医院id
	public String hos_name;// ：医院名
	public int sex;// ：性别 0未知 1男 2女
	public String age;// : 年龄
	public String department;// ： 科室
	public String ward_no;// : 病房号
	public String bed_no;// : 床位号
	public String diagnose;// : 诊断结果，如果是多个诊断结果，用&&分割，给客户端显示自行分割
	public String treat_state;// ： 就诊状态
	public String procedure_title;// ： 诊疗规范标题
	public String procedure_text;// ： 诊疗规范内容
	public String abs;// ： 摘要
	public int state;// : 0正常 1临时
	public int create_time;// : 病例时间
	public DoctorInfo admin_doctor;// : 管理员
	public List<DoctorInfo> participate_doctor;// : 管理员

	public static class DoctorInfo implements Serializable {
		/**
	 * 
	 */
		private static final long serialVersionUID = 5674389516689554457L;
		public String id;// : id
		public String name;// : 姓名
	}

}
