package com.rmtech.qjys.model.gson;

import java.util.List;

import com.rmtech.qjys.model.HospitalInfo;

public class MHosList extends MBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4973441121018076002L;
	public List<HospitalInfo>  data;
	public boolean isEmpty() {
		return data == null || data.isEmpty();
	}

	
}
