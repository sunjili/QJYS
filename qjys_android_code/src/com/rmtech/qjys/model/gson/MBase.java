package com.rmtech.qjys.model.gson;

import java.io.Serializable;

import okhttp3.Response;
import android.util.Log;

import com.google.gson.Gson;
import com.rmtech.qjys.QjApplication;
import com.rmtech.qjys.callback.QjHttpCallback;

public class MBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int ret;//
	public String msg;


}
