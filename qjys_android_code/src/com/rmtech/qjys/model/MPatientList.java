package com.rmtech.qjys.model;

import java.io.Serializable;
import java.util.List;

public class MPatientList extends MBase {

	private static final long serialVersionUID = 6439396671890103561L;
	/**
	 * 返回结构 { ret: 0， 其他失败 msg: 成功 data: { lists : {[ 医院名： [{ 'id' : 病例id 'name'
	 * : 姓名 'sex' : 性别 'age' : 年龄 hos_id : 医院id hos_name : 医院名 ward_no : 病房号
	 * bed_no : 床位号 create_time : 病例时间 admin_doctor : 管理员 { id : id name : 姓名 }
	 * participate_doctor: [{ 参与医生 id : id name : 姓名 }] treat_state ： 就诊状态
	 * department： 科室 diagnose : 诊断结果，如果是多个诊断结果，用&&分割，给客户端显示自行分割 }] ]} } }
	 * 
	 * { "ret": 0, "msg": "成功", "data": {"lists": {"北移三元": [ { "id": "107",
	 * "name": "新病人", "sex": "2", "age": "127", "hos_id": "101", "hos_name":
	 * "是的看法", "department": "pro哦哦", "ward_no": "18", "bed_no": "28",
	 * "diagnose": "", "treat_state": "", "doc_id": "107", "create_time":
	 * "1460736124", "admin_doctor": { "id": "107", "name": "das" },
	 * "participate_doctor": [] }, { "id": "105", "name": "新病人", "sex": "1",
	 * "age": "0", "hos_id": "101", "hos_name": "åŒ\u2014åŒ»ä¸\u2030é™¢",
	 * "department": "", "ward_no": "", "bed_no": "", "diagnose": "",
	 * "treat_state": "", "doc_id": "107", "create_time": "1460734338",
	 * "admin_doctor": { "id": "107", "name": "das" }, "participate_doctor": []
	 * }, { "id": "104", "name": "新病人", "sex": "1", "age": "0", "hos_id": "101",
	 * "hos_name": "史蒂夫", "department": "", "ward_no": "", "bed_no": "",
	 * "diagnose": "", "treat_state": "", "doc_id": "107", "create_time":
	 * "1460733575", "admin_doctor": { "id": "107", "name": "das" },
	 * "participate_doctor": [] }, { "id": "103", "name": "新病人", "sex": "1",
	 * "age": "0", "hos_id": "101", "hos_name": "阿福", "department": "",
	 * "ward_no": "", "bed_no": "", "diagnose": "", "treat_state": "", "doc_id":
	 * "107", "create_time": "1460733525", "admin_doctor": { "id": "107",
	 * "name": "das" }, "participate_doctor": [] } ]}} }
	 */

	public PatientList data;

	public static class PatientList implements Serializable {

		private static final long serialVersionUID = -2470015029770953699L;
		public List<HospitalCaseInfo> lists;
	}
	
	public static class HospitalCaseInfo implements Serializable {

		private static final long serialVersionUID = 9018568348573769431L;
		public String hos_fullname;// : 医院全称
		public String hos_shortname;// :  医院简称
		public String hos_id;//:id
		public List<CaseInfo> patients;//:
		
	}


	public boolean hasData() {
		return data != null && data.lists != null && !data.lists.isEmpty();
	}
}
