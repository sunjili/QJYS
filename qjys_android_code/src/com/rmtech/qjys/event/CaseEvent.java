package com.rmtech.qjys.event;

import java.util.ArrayList;

import com.rmtech.qjys.model.DoctorInfo;

public class CaseEvent {

	public static final int TYPE_ADD = 1;
	public static final int TYPE_EDIT = 2;
	public static final int TYPE_DELETE = 3;
	public static final int TYPE_GROUP_CHANGED_ADD = 4;
	public static final int TYPE_GROUP_CHANGED_DELETE = 5;
	public static final int TYPE_GROUP_CHANGED_ADMIN = 6;
	public int type;
	public ArrayList<DoctorInfo> deleteDoctorList;
	public ArrayList<DoctorInfo> addDoctorList;
	public String caseInfoId;

	public CaseEvent(int type) {
		this.type = type;
	}

	public void setDeleteDoctorList(String caseInfoId, ArrayList<DoctorInfo> resultList) {
		this.deleteDoctorList = resultList;
		this.caseInfoId = caseInfoId;
	}

	public void setAddDoctorList(String caseInfoId, ArrayList<DoctorInfo> resultList) {
		this.addDoctorList = resultList;
		this.caseInfoId = caseInfoId;
	}

}
