package com.rmtech.qjys.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.R;
import com.sjl.lib.multi_image_selector.MultiImageSelectorActivity;

public class PhotoUtil {
	public static void showCameraAction(Activity context, File mTmpFile) {
		// 跳转到系统照相机
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		if (cameraIntent.resolveActivity(context.getPackageManager()) != null) {
			// 设置系统相机拍照后的输出路径
			// 创建临时文件
			// String name = new
			// DateFormat().format("yyyyMMdd_hhmmss",Calendar.getInstance(Locale.CHINA))
			// + ".jpg";
			
			if (mTmpFile != null && mTmpFile.exists()) {
				cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mTmpFile));
				context.startActivityForResult(cameraIntent, QjConstant.REQUEST_CAMERA);
			} else {
				Toast.makeText(context, "存储空间不足", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(context, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
		}
	}

	public static void showChooser(Activity context) {
		// Use the GET_CONTENT intent from the utility class
		Intent target = com.sjl.lib.filechooser.FileUtils.createGetContentIntent();
		// Create the chooser Intent
		Intent intent = Intent.createChooser(target, "chooser_title");
		try {
			context.startActivityForResult(intent, QjConstant.REQUEST_CODE);
		} catch (ActivityNotFoundException e) {
			// The reason for the existence of aFileChooser
		}
	}

	public static void startImageSelector(Activity context, boolean multiSelect) {

		Intent intent = new Intent(context, MultiImageSelectorActivity.class);
		// 是否显示拍摄图片
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, false);
		// 最大可选择图片数量
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, multiSelect?9:1);
		// 选择模式
		int mode = multiSelect? MultiImageSelectorActivity.MODE_MULTI :  MultiImageSelectorActivity.MODE_SINGLE;
		intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, mode);
		// 默认选择
		// if(mSelectPath != null && mSelectPath.size()>0){
		// intent.putExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST,
		// mSelectPath);
		// }
		context.startActivityForResult(intent, QjConstant.REQUEST_IMAGE);
	}

	public static String getPhotoFileName() {
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
	
	public static final String STORAGE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
	public static final String SAILFISH_DIR = STORAGE_DIR + "/qjys";
	public static final File IMAGE_CROP_TEMP_FILE = new File(SAILFISH_DIR, "crop_temp.jpg");
	public static final File IMAGE_ORIGI_FILE_DIR = new File(SAILFISH_DIR + "/picture_original");
	public static final File IMAGE_FINAL_FILE_DIR = new File(SAILFISH_DIR + "/picture_final");
	public static final int PHOTO_REQUEST_CUT_HEADER = 6;
	public static final String PIC_ZOOM_OUTPUT_FILE_KEY2 = "PIC_ZOOM_OUTPUT_FILE_KEY2";
	static {
		if (!IMAGE_ORIGI_FILE_DIR.exists()) {
			IMAGE_ORIGI_FILE_DIR.mkdirs();
		}
		
		if (!IMAGE_FINAL_FILE_DIR.exists()) {
			IMAGE_FINAL_FILE_DIR.mkdirs();
		}
	}


}
