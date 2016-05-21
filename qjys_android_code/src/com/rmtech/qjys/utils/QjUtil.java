package com.rmtech.qjys.utils;

import java.nio.charset.Charset;

import android.text.TextUtils;

public class QjUtil {
//	int len = str.toString().getBytes(Charset.forName("utf-8")).length;
//	int left = (SEND_BYTE_LIMIT - len) / 3 - ((SEND_BYTE_LIMIT - len) % 3 == 0 ? 0 : 1);
	public static int getStringBytesLen(String str) {
		if(TextUtils.isEmpty(str)) {
			return 0;
		}
		return str.toString().getBytes(Charset.forName("utf-8")).length;
	}
}
