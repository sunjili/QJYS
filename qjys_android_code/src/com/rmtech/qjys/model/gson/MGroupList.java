package com.rmtech.qjys.model.gson;

import java.util.List;

import com.rmtech.qjys.model.CaseInfo;

public class MGroupList extends MBase {

	/**
	 * 04-20 19:03:00.301: E/QjHttpCallback.java(21730):
	 * [parseNetworkResponse:19]response =
	 * {"ret":0,"msg":"\u6210\u529f","data":[
	 * {"id":"199","group_id":"187244327792017860"
	 * ,"name":"hhh","sex":"1","age":"0"
	 * ,"hos_id":"0","hos_name":"\u5176\u4ed6","department"
	 * :"","ward_no":"","bed_no"
	 * :"","diagnose":"","treat_state":"","doc_id":"101"
	 * ,"create_time":"1461161015"
	 * ,"admin_doctor":{"id":"101","name":"das","head"
	 * :""},"participate_doctor":[{"id":102,"name":"ks","head":""}]}]}
	 */
	private static final long serialVersionUID = 798364988032033026L;
	public List<CaseInfo> data;

	public boolean isEmpty() {
		return data == null || data.isEmpty();
	}

}
