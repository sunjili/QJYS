package com.sjl.lib.utils;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.os.Environment;

import com.rmtech.qjys.QjApplication;
import com.sjl.lib.filechooser.FileUtils;

/**
 * 
 * @author sunjili
 */
public final class StorageUtils {

	private static final String TAG = StorageUtils.class.getSimpleName();

	private StorageUtils() {
	}

	public static void deleteFile(File file) {
		if(file == null) {
			return;
		}
		if (file.isFile()) {
			file.delete();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				deleteFile(f);
			}
			file.delete();
		}
	}

	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		java.io.File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				size = size + getFolderSize(fileList[i]);
			} else {
				size = size + fileList[i].length();
			}
		}
		return size;
	}

	public static long getFolderSizeWithoutChildFolder(File file)
			throws Exception {
		long size = 0;
		java.io.File[] fileList = file.listFiles();
		for (int i = 0; i < fileList.length; i++) {
			if (!fileList[i].isDirectory()) {
				size = size + fileList[i].length();
			}
		}
		return size;
	}

	public static long getImageLoaderDiscCacheSize() throws Exception {
		File individualCacheDir = com.nostra13.universalimageloader.utils.StorageUtils
				.getCacheDirectory(QjApplication.getInstance());
		if (individualCacheDir == null || !individualCacheDir.exists()) {
			return 0;
		}
		return StorageUtils.getFolderSize(individualCacheDir);
	}

	public static long getDiscCacheSize() throws Exception {
		File individualCacheDir = FileUtils.getCacheDirectory(QjApplication
				.getInstance());
		if (individualCacheDir == null || !individualCacheDir.exists()) {
			return 0;
		}
		return StorageUtils.getFolderSize(individualCacheDir);
	}

	public static long getTotalDiscCacheSize() throws Exception {
		return getImageLoaderDiscCacheSize() + getDiscCacheSize();
	}

	public static void clearCache() {
		File file = FileUtils.getCacheDirectory(QjApplication.getInstance());
		if(file != null) {
			deleteFile(file);
		}
	}
}
