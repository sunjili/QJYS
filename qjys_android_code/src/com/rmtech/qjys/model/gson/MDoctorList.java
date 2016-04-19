package com.rmtech.qjys.model.gson;

import java.util.List;

import com.rmtech.qjys.model.DoctorInfo;

public class MDoctorList extends MBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = 401903725868489647L;
	public List<DoctorInfo>  data;
	public boolean isEmpty() {
		return data == null || data.isEmpty();
	}

	
}
