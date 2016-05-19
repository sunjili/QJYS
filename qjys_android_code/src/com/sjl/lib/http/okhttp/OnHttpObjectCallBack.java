package com.sjl.lib.http.okhttp;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

public abstract class OnHttpObjectCallBack<T> {
	Type type;

	public abstract void onSuccess(T T);

	public OnHttpObjectCallBack() {
		type = getSuperclassTypeParameter(getClass());
	}

	public void onSuccess(String json) {
		T t = new Gson().fromJson(json, type);
		try {
			onSuccess(t);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Returns the type from super class's type parameter in
	 * {@link $Gson$Types#canonicalize canonical form}.
	 */
	static Type getSuperclassTypeParameter(Class<?> subclass) {
		Type superclass = subclass.getGenericSuperclass();
		if (superclass instanceof Class) {
			throw new RuntimeException("Missing type parameter.");
		}
		ParameterizedType parameterized = (ParameterizedType) superclass;
		return $Gson$Types
				.canonicalize(parameterized.getActualTypeArguments()[0]);
	}
}