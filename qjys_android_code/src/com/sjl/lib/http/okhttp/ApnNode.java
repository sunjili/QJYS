package com.sjl.lib.http.okhttp;

public class ApnNode {
	private static final String TAG = ApnNode.class.getSimpleName();
	String apn;
	String name;

	public ApnNode(String apn, String name, String type) {
		this.apn = apn;
		this.name = name;
	}

	public ApnNode() {
	}

	public String getApn() {
		return this.apn;
	}

	public String getName() {
		return this.name;
	}

	public void setApn(String apn) {
		this.apn = apn;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" apn = " + this.apn);
		sb.append(" name = " + this.name);

		return sb.toString();
	}
}
