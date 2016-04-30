package com.rmtech.qjys.event;

import java.util.ArrayList;

import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.utils.GroupAndCaseListManager;

public class CaseEvent {

	public static final int TYPE_ADD = 1;
	public static final int TYPE_EDIT = 2;
	public static final int TYPE_DELETE = 3;
	public static final int TYPE_GROUP_CHANGED_ADD = 4;
	public static final int TYPE_GROUP_CHANGED_DELETE = 5;
	public static final int TYPE_GROUP_CHANGED_ADMIN = 6;
	public static final int TYPE_FLOW_CHANGED = 7;
	public int type;
	// public ArrayList<DoctorInfo> deleteDoctorList;
	// public ArrayList<DoctorInfo> addDoctorList;
	public String caseInfoId;

	public CaseEvent(int type) {
		this.type = type;
	}

	public void setCaseInfoId(String caseInfoId) {
		this.caseInfoId = caseInfoId;
	}

}
