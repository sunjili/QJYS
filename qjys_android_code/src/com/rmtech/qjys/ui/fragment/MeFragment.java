package com.rmtech.qjys.ui.fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.qjactivity.ImageCropActivity;
import com.rmtech.qjys.ui.qjactivity.MeAboutActivity;
import com.rmtech.qjys.ui.qjactivity.MeCleanMemoryActivity;
import com.rmtech.qjys.ui.qjactivity.MeFlowActivity;
import com.rmtech.qjys.ui.qjactivity.MeHospitalActivity;
import com.rmtech.qjys.ui.qjactivity.MeNameActivity;
import com.rmtech.qjys.ui.qjactivity.MePasswordChangeActivity;
import com.rmtech.qjys.ui.qjactivity.MePhoneActivity;
import com.rmtech.qjys.ui.qjactivity.MeRoomActivity;
import com.rmtech.qjys.ui.qjactivity.MeSexActivity;
import com.rmtech.qjys.ui.qjactivity.MeTreatmentStateActivity;
import com.rmtech.qjys.ui.view.MeItemLayout;
import com.rmtech.qjys.utils.PhotoUtil;
import com.sjl.lib.alertview.AlertView;
import com.sjl.lib.filechooser.FileUtils;
import com.sjl.lib.multi_image_selector.MultiImageSelectorActivity;

/**
 *我  界面
 * 
 * 
 */
public class MeFragment extends QjBaseFragment implements OnClickListener {
	/***  头像  */
	private ImageView iv_head;
	/***   */
	private RelativeLayout rl_head;
	/**  名字         */
	private MeItemLayout  me_name;
	/**  性别         */
	private MeItemLayout  me_sex;
	/**  医院        */
	private MeItemLayout  me_hospital;
	/**  科室         */
	private MeItemLayout  me_room;
	/**  手机号         */
	private MeItemLayout  me_phone;
	/**  登录密码         */
	private MeItemLayout  me_password;
	/**  病例就诊状态        */
	private MeItemLayout  me_treatment_state;
	/**  临床诊疗规范及流程         */
	private MeItemLayout  me_standard_and_flow;
	/** 清理存储空间         */
	private MeItemLayout  me_clear_memory;
	/**  病例回收站         */
	private MeItemLayout  me_recycle;
	/**  关于奇迹医生         */
	private MeItemLayout  me_about;
	/**   退出登录	 */
	private Button btn_logout;

	private FragmentActivity context;
	
	private void setViewValue() {
		me_name.setRightText("111111");
		me_sex.setRightText("111111");
		me_hospital.setRightText("111111");
		me_room.setRightText("111111");
		
		me_phone.setRightText("13812345678");
		me_password.setRightText("");
		
//		me_treatment_state.setRightText("111111");
//		me_standard_and_flow.setRightText("111111");
//		me_clear_memory.setRightText("111111");
//		me_recycle.setRightText("111111");
//		me_about.setRightText("111111");

	}
	private File mTmpFile;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case QjConstant.REQUEST_CODE:
			// If the file selection was successful
			if (resultCode == getActivity().RESULT_OK) {
				if (data != null) {
					// Get the URI of the selected file
					final Uri uri = data.getData();
					Log.i("ssssssssss", "Uri = " + uri.toString());
					try {
						// Get the file path from the URI
						final String path = FileUtils.getPath(getActivity(), uri);
						Toast.makeText(getActivity(), "File Selected: " + path, Toast.LENGTH_LONG).show();
					} catch (Exception e) {
						Log.e("FileSelectorTestActivity", "File select error", e);
					}
				}
			}
			break;

		case QjConstant.REQUEST_IMAGE:

			if (resultCode == getActivity().RESULT_OK) {
				ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				// StringBuilder sb = new StringBuilder();
				// for(String p: mSelectPath){
				// sb.append(p);
				// sb.append("\n");
				// }
				// mResultText.setText(sb.toString());
			}
			break;

		case QjConstant.REQUEST_CAMERA:

			if (resultCode == Activity.RESULT_OK) {
				if (mTmpFile != null) {
					if (mTmpFile != null) {
						getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(mTmpFile)));

						// Intent data = new Intent();
						// resultList.add(imageFile.getAbsolutePath());
						// data.putStringArrayListExtra(EXTRA_RESULT,
						// resultList);
						// setResult(RESULT_OK, data);
						// finish();
						// mCallback.onCameraShot(mTmpFile);
					}
				} else {
					while (mTmpFile != null && mTmpFile.exists()) {
						boolean success = mTmpFile.delete();
						if (success) {
							mTmpFile = null;
						}
					}
				}
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_head:
			Toast.makeText(context, "头像", Toast.LENGTH_SHORT).show();

			new AlertView(null, null, "取消", null, new String[] { "拍照", "从手机相册中选择", "从资源管理器中选择" },
					getActivity(), AlertView.Style.ActionSheet,
					new com.sjl.lib.alertview.OnItemClickListener() {

						@Override
						public void onItemClick(Object o, int position) {
							switch (position) {
							case 0:
								try {
									mTmpFile = FileUtils.createTmpFile(context);
								} catch (IOException e) {
									e.printStackTrace();
								}
								PhotoUtil.showCameraAction(getActivity(), mTmpFile);
								break;
							case 1:
								PhotoUtil.startImageSelector(getActivity(), false);
								// ImageSelectorMainActivity.show(PhotoDataManagerActivity.this);
								break;
							case 2:
								PhotoUtil.showChooser(getActivity());
								break;
							}

						}
					}).show();
			break;
		case R.id.me_name:
			jumpActivity(MeNameActivity.class);
			break;
		case R.id.me_sex:
			jumpActivity(MeSexActivity.class);
		break;
		case R.id.me_hospital:
			jumpActivity(MeHospitalActivity.class);
		break;
		case R.id.me_room:
			jumpActivity(MeRoomActivity.class);
		break;
		
		
		case R.id.me_phone:
			jumpActivity(MePhoneActivity.class);
		break;
		case R.id.me_password:
			//设置新密码
//			jumpActivity(MePasswordNewActivity.class);
			//更改密码
			jumpActivity(MePasswordChangeActivity.class);
		break;
		
		
		case R.id.me_treatment_state:
			jumpActivity(MeTreatmentStateActivity.class);
		break;
		case R.id.me_standard_and_flow:
			jumpActivity(MeFlowActivity.class);
		break;
		case R.id.me_clear_memory:    
		jumpActivity(MeCleanMemoryActivity.class);
		break;
		case R.id.me_recycle:
			Toast.makeText(context, "11111", Toast.LENGTH_SHORT).show();
		break;
		
		case R.id.me_about:
			jumpActivity(MeAboutActivity.class);

		break;
		case R.id.btn_logout:
			Toast.makeText(context, "退出登录", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
	private void jumpActivity(Class<?> cls) {
		Intent intent=new Intent(context,cls);
		startActivity(intent);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.em_fragment_me, container, false);
		context =MeFragment.this.getActivity();
		initView(inflate);
		return inflate;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setViewValue();
	}


	private void initView(View inflate) {
		me_name=(MeItemLayout) inflate.findViewById(R.id.me_name);
		me_name.setOnClickListener(this);
		me_sex=(MeItemLayout) inflate.findViewById(R.id.me_sex);
		me_sex.setOnClickListener(this);
		me_hospital=(MeItemLayout) inflate.findViewById(R.id.me_hospital);
		me_hospital.setOnClickListener(this);
		me_room=(MeItemLayout) inflate.findViewById(R.id.me_room);
		me_room.setOnClickListener(this);
		
		
		me_phone=(MeItemLayout) inflate.findViewById(R.id.me_phone);
		me_phone.setOnClickListener(this);
		me_password=(MeItemLayout) inflate.findViewById(R.id.me_password);
		me_password.setOnClickListener(this);
		
		
		me_treatment_state=(MeItemLayout) inflate.findViewById(R.id.me_treatment_state);
		me_treatment_state.setOnClickListener(this);
		me_standard_and_flow=(MeItemLayout) inflate.findViewById(R.id.me_standard_and_flow);
		me_standard_and_flow.setOnClickListener(this);
		me_clear_memory=(MeItemLayout) inflate.findViewById(R.id.me_clear_memory);
		me_clear_memory.setOnClickListener(this);
		me_recycle=(MeItemLayout) inflate.findViewById(R.id.me_recycle);
		me_recycle.setOnClickListener(this);
		me_about=(MeItemLayout) inflate.findViewById(R.id.me_about);
		me_about.setOnClickListener(this);
		
		
		btn_logout=(Button) inflate.findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(this);
		
		iv_head=(ImageView) inflate.findViewById(R.id.iv_head);
		rl_head=(RelativeLayout) inflate.findViewById(R.id.rl_head);
		rl_head.setOnClickListener(this);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void setUpView() {
		// TODO Auto-generated method stub
	}
	

	private void startPhotoZoom(Uri uri) {

		String fileName = PhotoUtil.getPhotoFileName();
//		SharedPreferences share = UserInfoActivity.this.getSharedPreferences(
//				CameraUtil.PIC_ZOOM_OUTPUT_DB, 0);
//		share.edit().putString(CameraUtil.PIC_ZOOM_OUTPUT_FILE_KEY2, fileName)
//				.commit();

		Uri imageUri = Uri.fromFile(new File(PhotoUtil.IMAGE_FINAL_FILE_DIR,
				fileName));
		Intent intent = new Intent(getActivity(),
				ImageCropActivity.class);
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("outputX", 120);// 输出图片大小
		intent.putExtra("outputY", 120);
		intent.putExtra("type", ImageCropActivity.TYPE_USER_HEAD);
		intent.putExtra("output", imageUri.toString());

		try {
			getActivity().startActivityForResult(intent,
					PhotoUtil.PHOTO_REQUEST_CUT_HEADER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
