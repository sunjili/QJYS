package com.rmtech.qjys.ui.fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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

import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.hx.QjHelper;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.UserInfo;
import com.rmtech.qjys.model.gson.MUrlData;
import com.rmtech.qjys.ui.LoginActivity;
import com.rmtech.qjys.ui.MainActivity;
import com.rmtech.qjys.ui.qjactivity.ImageCropActivity;
import com.rmtech.qjys.ui.qjactivity.MeAboutActivity;
import com.rmtech.qjys.ui.qjactivity.MeCleanMemoryActivity;
import com.rmtech.qjys.ui.qjactivity.MeFlowActivity;
import com.rmtech.qjys.ui.qjactivity.MeHospitalActivity;
import com.rmtech.qjys.ui.qjactivity.MeNameActivity;
import com.rmtech.qjys.ui.qjactivity.MePasswordChangeActivity;
import com.rmtech.qjys.ui.qjactivity.MePasswordNewActivity;
import com.rmtech.qjys.ui.qjactivity.MePhoneActivity;
import com.rmtech.qjys.ui.qjactivity.MeRecycleActivity;
import com.rmtech.qjys.ui.qjactivity.MeRoomActivity;
import com.rmtech.qjys.ui.qjactivity.MeSexActivity;
import com.rmtech.qjys.ui.qjactivity.MeTreatmentStateActivity;
import com.rmtech.qjys.ui.qjactivity.QjLoginActivity;
import com.rmtech.qjys.ui.view.MeItemLayout;
import com.rmtech.qjys.utils.PhotoUtil;
import com.sjl.lib.alertview.AlertView;
import com.sjl.lib.filechooser.FileUtils;
import com.sjl.lib.multi_image_selector.MultiImageSelectorActivity;
import com.sjl.lib.utils.L;

/**
 * 我 界面
 * 
 * 
 */
public class MeFragment extends QjBaseFragment implements OnClickListener {
	/*** 失败 */
	public static final int REQUEST_ME_FAIL = 0x4000;

	public static final int REQUEST_ME_HEAD = 0x4001;
	public static final int REQUEST_ME_NAME = 0x4002;
	public static final int REQUEST_ME_SEX = 0x4003;
	public static final int REQUEST_ME_HOSPITAL = 0x4004;
	public static final int REQUEST_ME_ROOM = 0x4005;
	public static final int REQUEST_ME_PHONE = 0x4006;
	public static final int REQUEST_ME_PASSWORD = 0x4007;
	public static final int REQUEST_ME_TREATMENT_STATE = 0x4008;
	public static final int REQUEST_ME_FLOW = 0x4009;
	public static final int REQUEST_ME_RECYCLE = 0x4010;

	private UserInfo meValue;
	/*** 头像 */
	private ImageView iv_head;
	/***   */
	private RelativeLayout rl_head;
	/** 名字 */
	private MeItemLayout me_name;
	/** 性别 */
	private MeItemLayout me_sex;
	/** 医院 */
	private MeItemLayout me_hospital;
	/** 科室 */
	private MeItemLayout me_room;
	/** 二维码 */
	private MeItemLayout me_qrcode;
	/** 手机号 */
	private MeItemLayout me_phone;
	/** 登录密码 */
	private MeItemLayout me_password;
	/** 病例就诊状态 */
	private MeItemLayout me_treatment_state;
	/** 临床诊疗规范及流程 */
	private MeItemLayout me_standard_and_flow;
	/** 清理存储空间 */
	private MeItemLayout me_clear_memory;
	/** 病例回收站 */
	private MeItemLayout me_recycle;
	/** 关于奇迹医生 */
	private MeItemLayout me_about;
	/** 退出登录 */
	private Button btn_logout;

	private FragmentActivity context;

	private void setViewValue() {
		me_name.setRightText(meValue.name);
		if (meValue.sex == 1) {
			me_sex.setRightText("男");
		} else {
			me_sex.setRightText("女");
		}
		me_hospital.setRightText(meValue.hos_fullname);
		me_room.setRightText(meValue.department);
		ImageLoader.getInstance().displayImage(meValue.head, iv_head, QjConstant.optionsHead);

		try {
			String phone = meValue.phone;
			me_phone.setRightText(phone.subSequence(0, 3) + "****" + phone.subSequence(8, phone.length()));
		} catch (Exception e) {
			me_phone.setRightText("");
			L.e("------设置我的界面手机号-------");
		}
		if (meValue.isset_passwd == 1) {
			me_password.setRightText("已设置");
		} else {
			me_password.setRightText("");
		}
		me_qrcode.setQRcodeRightDrawble();

	}

	private File mTmpFile;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		String string;
		switch (requestCode) {
		case QjConstant.REQUEST_CODE:
			// If the file selection was successful
			if (resultCode == getActivity().RESULT_OK) {
				if (data != null) {
					// Get the URI of the selected file
					final Uri uri = data.getData();
					Log.i("ssssssssss", "Uri = " + uri.toString());
					try {
						startPhotoZoom(uri);
					} catch (Exception e) {
						Log.e("FileSelectorTestActivity", "File select error", e);
					}
				}
			}
			break;

		case QjConstant.REQUEST_IMAGE:

			if (resultCode == getActivity().RESULT_OK) {
				ArrayList<String> mSelectPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
				if (mSelectPath != null && mSelectPath.size() > 0) {
					startPhotoZoom(Uri.fromFile(new File(mSelectPath.get(0))));
				}
			}
			break;

		case QjConstant.REQUEST_CAMERA:

			if (resultCode == Activity.RESULT_OK) {
				if (mTmpFile != null) {
					if (mTmpFile != null) {
						Uri uri = Uri.fromFile(mTmpFile);
						getActivity().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
						startPhotoZoom(uri);
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
		case PhotoUtil.PHOTO_REQUEST_CUT_HEADER:
			if (resultCode == Activity.RESULT_OK) {
				if (data != null && !data.getBooleanExtra("qualified", true)) {
					// 提示用户:所选图片太小
					new AlertDialog.Builder(getActivity()).setMessage(String.format("图片尺寸不能小于%d*%d", 480, 480))
							.setPositiveButton("我知道了", null).show();
					break;
				}

				String path = new File(PhotoUtil.IMAGE_FINAL_FILE_DIR, mPhotoZoomFileName).getAbsolutePath();

				ImageLoader.getInstance().displayImage("file://" + path, iv_head, QjConstant.optionsHead);
				QjHttp.uploadHead(path, new QjHttpCallback<MUrlData>() {

					@Override
					public MUrlData parseNetworkResponse(String str) throws Exception {
						return new Gson().fromJson(str, MUrlData.class);
					}

					@Override
					public void onResponseSucces(MUrlData response) {
						if (response.data != null) {
							Toast.makeText(context, "头像上传成功", Toast.LENGTH_LONG).show();
							UserContext.getInstance().setUserHead(response.data.url);
						}
					}

					@Override
					public void onError(Call call, Exception e) {
						Toast.makeText(context, "头像上传失败", Toast.LENGTH_LONG).show();
					}
				});

				// Bitmap bitmap = BitmapFactory.decodeFile();
				// if(bitmap == null) {
				// Toast.makeText(context, "头像裁剪失败", Toast.LENGTH_LONG).show();
				// } else {
				// iv_head.setImageBitmap(bitmap);
				// }
			}
			break;

		// case REQUEST_ME_HEAD:
		// string = data.getStringExtra("string");
		// meValue.imgUrl=string;
		// setViewValue();
		// break;
		case REQUEST_ME_NAME:
			if (resultCode == Activity.RESULT_OK) {
				string = data.getStringExtra("string");
				meValue.name = string;
				setViewValue();
			}
			break;
		case REQUEST_ME_SEX:
			if (resultCode == Activity.RESULT_OK) {
				int sex = data.getIntExtra("int", meValue.sex);
				meValue.sex = sex;
				setViewValue();
				break;
			}
		case REQUEST_ME_HOSPITAL:
			if (resultCode == Activity.RESULT_OK) {
				string = data.getStringExtra("string");
				meValue.hos_fullname = string;
				setViewValue();
			}

			break;
		case REQUEST_ME_ROOM:
			if (resultCode == Activity.RESULT_OK) {
				string = data.getStringExtra("string");
				meValue.department = string;
				setViewValue();
			}

			break;
		case REQUEST_ME_PHONE:
			if (resultCode == Activity.RESULT_OK) {
				string = data.getStringExtra("string");
				meValue.phone = string;
				setViewValue();
			}
			break;
		case REQUEST_ME_PASSWORD:
			if (resultCode == Activity.RESULT_OK) {
				boolean bool = data.getBooleanExtra("boolean", false);
				meValue.isset_passwd = bool ? 1 : 0;
				setViewValue();
			}
			break;
		default:
			break;

		}

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_head:
//			Toast.makeText(context, "头像", Toast.LENGTH_SHORT).show();

			new AlertView(null, null, "取消", null, new String[] { "拍照", "从手机相册中选择", "从资源管理器中选择" }, getActivity(),
					AlertView.Style.ActionSheet, new com.sjl.lib.alertview.OnItemClickListener() {

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
			// MeNameActivity.show(context);
			jumpActivity(MeNameActivity.class, REQUEST_ME_NAME);
			break;
		case R.id.me_sex:
			jumpActivity(MeSexActivity.class, REQUEST_ME_SEX);
			break;
		case R.id.me_hospital:
			jumpActivity(MeHospitalActivity.class, REQUEST_ME_HOSPITAL);
			break;
		case R.id.me_room:
			jumpActivity(MeRoomActivity.class, REQUEST_ME_ROOM);
			break;
		case R.id.me_qrcode:
			//TODO 我的二维码页面
			
			break;
		case R.id.me_phone:
			jumpActivity(MePhoneActivity.class, REQUEST_ME_PHONE, meValue.phone);
			break;
		case R.id.me_password:
			if (meValue.isset_passwd == 1) {
				// 更改密码
				jumpActivity(MePasswordChangeActivity.class, REQUEST_ME_PASSWORD);
			} else {
				// 设置新密码
				Intent intent = new Intent(context, MePasswordNewActivity.class);
				intent.putExtra("isMe", 1);
				startActivityForResult(intent, REQUEST_ME_PASSWORD);
			}
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
			jumpActivity(MeRecycleActivity.class);
			break;

		case R.id.me_about:
			jumpActivity(MeAboutActivity.class);

			break;
		case R.id.btn_logout:
			logout();
//			Toast.makeText(context, "退出登录", Toast.LENGTH_SHORT).show();
			// btn_logout.setVisibility(View.GONE);
			// meValue = new UserInfo();
			// setViewValue();
			break;
		default:
			break;
		}
	}

	void logout() {
		UserContext.getInstance().clear();
		QjLoginActivity.show(getActivity());
		QjHelper.getInstance().logout(false, new EMCallBack() {

			@Override
			public void onSuccess() {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						// 重新显示登陆页面
						//((MainActivity) getActivity()).finish();
					}
				});
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, String message) {
			}
		});
	}

	private void jumpActivity(Class<?> cls) {
		Intent intent = new Intent(context, cls);
		startActivity(intent);
	}

	private void jumpActivity(Class<?> cls, int requestCode) {
		Intent intent = new Intent(context, cls);
		startActivityForResult(intent, requestCode);
	}

	private void jumpActivity(Class<?> cls, int requestCode, String str) {
		Intent intent = new Intent(context, cls);
		intent.putExtra("string", str);
		startActivityForResult(intent, requestCode);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View inflate = inflater.inflate(R.layout.em_fragment_me, container, false);
		context = MeFragment.this.getActivity();
		meValue = UserContext.getInstance().getUser();
		initView(inflate);
		// TODO 保存value
		setViewValue();
		return inflate;
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	private void initView(View inflate) {
		me_name = (MeItemLayout) inflate.findViewById(R.id.me_name);
		me_name.setOnClickListener(this);
		me_sex = (MeItemLayout) inflate.findViewById(R.id.me_sex);
		me_sex.setOnClickListener(this);
		me_hospital = (MeItemLayout) inflate.findViewById(R.id.me_hospital);
		me_hospital.setOnClickListener(this);
		me_room = (MeItemLayout) inflate.findViewById(R.id.me_room);
		me_room.setOnClickListener(this);
		me_qrcode = (MeItemLayout) inflate.findViewById(R.id.me_qrcode);
		me_qrcode.setOnClickListener(this);
		me_phone = (MeItemLayout) inflate.findViewById(R.id.me_phone);
		me_phone.setOnClickListener(this);
		me_password = (MeItemLayout) inflate.findViewById(R.id.me_password);
		me_password.setOnClickListener(this);

		me_treatment_state = (MeItemLayout) inflate.findViewById(R.id.me_treatment_state);
		me_treatment_state.setOnClickListener(this);
		me_standard_and_flow = (MeItemLayout) inflate.findViewById(R.id.me_standard_and_flow);
		me_standard_and_flow.setOnClickListener(this);
		me_clear_memory = (MeItemLayout) inflate.findViewById(R.id.me_clear_memory);
		me_clear_memory.setOnClickListener(this);
		me_recycle = (MeItemLayout) inflate.findViewById(R.id.me_recycle);
		me_recycle.setOnClickListener(this);
		me_about = (MeItemLayout) inflate.findViewById(R.id.me_about);
		me_about.setOnClickListener(this);

		btn_logout = (Button) inflate.findViewById(R.id.btn_logout);
		btn_logout.setOnClickListener(this);

		iv_head = (ImageView) inflate.findViewById(R.id.iv_head);

		rl_head = (RelativeLayout) inflate.findViewById(R.id.rl_head);
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

	private String mPhotoZoomFileName;

	private void startPhotoZoom(Uri uri) {

		mPhotoZoomFileName = PhotoUtil.getPhotoFileName();

		Uri imageUri = Uri.fromFile(new File(PhotoUtil.IMAGE_FINAL_FILE_DIR, mPhotoZoomFileName));
		Intent intent = new Intent(getActivity(), ImageCropActivity.class);
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("outputX", 120);// 输出图片大小
		intent.putExtra("outputY", 120);
		intent.putExtra("type", ImageCropActivity.TYPE_USER_HEAD);
		intent.putExtra("output", imageUri.toString());

		try {
			getActivity().startActivityForResult(intent, PhotoUtil.PHOTO_REQUEST_CUT_HEADER);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
