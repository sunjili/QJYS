package com.sjl.lib.db.dao;

import com.j256.ormlite.field.DatabaseField;

public class DBCache {
	@DatabaseField(id = true)
	public String key;

	@DatabaseField(canBeNull=false)
	public String value;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public DBCache() {
		// TODO Auto-generated constructor stub
	}

}
