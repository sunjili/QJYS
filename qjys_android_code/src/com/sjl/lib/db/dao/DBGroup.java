package com.sjl.lib.db.dao;

import com.j256.ormlite.field.DatabaseField;

public class DBGroup {
	@DatabaseField(id = true)
	public String groupID;


	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public DBGroup() {
		// TODO Auto-generated constructor stub
	}
	public DBGroup(String string, String string2) {
		groupID = string2;
	}

	public DBGroup(String groupid) {
		groupID = groupid;
	}



}
