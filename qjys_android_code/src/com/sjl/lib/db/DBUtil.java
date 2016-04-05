package com.sjl.lib.db;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.rmtech.qjys.QjApplication;
import com.sjl.lib.db.dao.DBCache;
import com.sjl.lib.utils.SerializeObject;

@SuppressWarnings("unchecked")
public class DBUtil {

	public static boolean saveCache(final String key, final Serializable model) {
		try {
			Context context = QjApplication.getInstance()
					.getApplicationContext();
			if (context == null) {
				return false;
			}
			if (TextUtils.isEmpty(key)) {
				return false;
			}
			String ser = SerializeObject.objectToString(model);
			DBCache cache = new DBCache();
			cache.setKey(key);
			cache.setValue(ser);
			Dao<DBCache, Integer> dao = DatabaseHelper.getHelper(context)
					.getDao(DBCache.class);
			dao.createOrUpdate(cache);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void saveCacheAsync(final String key, final Serializable model) {
		// new AsyncTask<Params, Progress, Result>() {
		new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				return saveCache(key, model);
			}

		}.execute();
	}

	public static Object getCache(final String key) {
		try {
			Context context = QjApplication.getInstance()
					.getApplicationContext();
			if (context == null) {
				return null;
			}
			Dao<DBCache, Integer> dao = DatabaseHelper.getHelper(context)
					.getDao(DBCache.class);
			List<DBCache> list = dao.queryBuilder().where().eq("key", key)
					.query();
			if (list == null || list.size() == 0) {
				return null;
			}
			String ser = list.get(0).value;
			// Log.d(TAG,"readCache  list.size()="+list.size());
			// Log.d(TAG,"readCache  ser="+ser);
			return SerializeObject.stringToObject(ser);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
