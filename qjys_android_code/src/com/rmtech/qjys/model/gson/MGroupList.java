package com.rmtech.qjys.model.gson;

import java.util.List;

import com.rmtech.qjys.model.CaseInfo;

public class MGroupList extends MBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 798364988032033026L;
	public List<CaseInfo> data;

	public boolean isEmpty() {
		return data == null || data.isEmpty();
	}

}
