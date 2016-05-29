package com.rmtech.qjys.ui.qjactivity;

import java.util.ArrayList;

import okhttp3.Call;

import org.greenrobot.eventbus.EventBus;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rmtech.qjys.QjConstant;
import com.rmtech.qjys.QjHttp;
import com.rmtech.qjys.R;
import com.rmtech.qjys.callback.BaseModelCallback;
import com.rmtech.qjys.callback.QjHttpCallback;
import com.rmtech.qjys.event.CaseEvent;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.model.gson.MBase;
import com.rmtech.qjys.model.gson.MFlowList.FlowInfo;
import com.rmtech.qjys.model.gson.MIdData;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.GroupDetailsActivity;
import com.rmtech.qjys.ui.view.DiagnoseAddView;
import com.rmtech.qjys.ui.view.MeItemLayout;

@SuppressLint("NewApi")
public class AddCaseActivity extends BaseActivity implements OnClickListener {
	private TextView nameTv;
	private EditText nameEt;
	private TextView genderTv;
	private TextView genderWomanTv;
	private TextView genderManTv;
	private TextView ageTv;
	private TextView ageTitleTv;
	private EditText ageEt;
	private ImageView hosArrowImage;
	private TextView hospitalTv;
	private TextView keshiTv;
	private EditText keshiEt;
	private TextView roomTv;
	private TextView roomTitleTv;
	private EditText roomEt;
	private TextView bedTv;
	private TextView bedTitleTv;
	private EditText bedEt;
	private DiagnoseAddView diagnose_view;
	private MeItemLayout stateLayout;
	private RelativeLayout photoDataLayout;
	private RelativeLayout doctorsLayout;
	private RelativeLayout ruleLayout;
	private RelativeLayout abstractLayout;
	private TextView abstractTv;
	private EditText abstractEt;
	private int selectSex = 0;
	// private ArrayList<String> diagnoseList = new ArrayList<String>();
	private String treat_state;
	private String currentHospital;
	private String tempCaseId;
	private ArrayList<DoctorInfo> currentDoctorList = new ArrayList<DoctorInfo>();

	// private String procedure_title;
	// private String procedure_text;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qjactivity_add_case);
		setTitle("添加病例");
		setLeftTitle("病例列表");
		setRightTitle("保存", new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (TextUtils.isEmpty(tempCaseId)) {
					save(0, 0);
				} else {
					CaseInfo info = createCaseInfo(0);
					info.id = tempCaseId;
					QjHttp.updatepatient(info, new BaseModelCallback() {

						@Override
						public void onError(Call call, Exception e) {
							Toast.makeText(getActivity(),
									"新病例创建失败 " + e.getMessage(),
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onResponseSucces(MBase response) {
							Toast.makeText(getActivity(), "新病例创建成功",
									Toast.LENGTH_SHORT).show();
							EventBus.getDefault().post(
									new CaseEvent(CaseEvent.TYPE_ADD));
							finish();
						}
					});
				}
			}
		});

		nameTv = (TextView) findViewById(R.id.name_tv);
		nameEt = (EditText) findViewById(R.id.name_et);
		setTextWhacher(AddCaseActivity.this, nameEt, 60);
		genderTv = (TextView) findViewById(R.id.gender_tv);
		genderWomanTv = (TextView) findViewById(R.id.gender_woman_tv);
		genderWomanTv.setOnClickListener(this);
		genderManTv = (TextView) findViewById(R.id.gender_man_tv);
		genderManTv.setOnClickListener(this);
		ageTv = (TextView) findViewById(R.id.age_tv);
		ageTitleTv = (TextView) findViewById(R.id.age_title_tv);
		ageEt = (EditText) findViewById(R.id.age_et);
		
		hosArrowImage = (ImageView) findViewById(R.id.hos_arrow_image);
		findViewById(R.id.hospital_layout).setOnClickListener(this);
		hospitalTv = (TextView) findViewById(R.id.hospital_tv);
		// 医院
		hospitalTv.setText(UserContext.getInstance().getUser().hos_fullname);
		currentHospital = UserContext.getInstance().getUser().hos_fullname;
		keshiTv = (TextView) findViewById(R.id.keshi_tv);
		keshiEt = (EditText) findViewById(R.id.keshi_et);
		setTextWhacher(AddCaseActivity.this, keshiEt, 60);
		keshiEt.setText(UserContext.getInstance().getUser().department);
		roomTv = (TextView) findViewById(R.id.room_tv);
		roomTitleTv = (TextView) findViewById(R.id.room_title_tv);
		roomEt = (EditText) findViewById(R.id.room_et);
		setTextWhacher(AddCaseActivity.this, roomEt, 30);
		bedTv = (TextView) findViewById(R.id.bed_tv);
		bedTitleTv = (TextView) findViewById(R.id.bed_title_tv);
		bedEt = (EditText) findViewById(R.id.bed_et);
		setTextWhacher(AddCaseActivity.this, bedEt, 30);
		diagnose_view = (DiagnoseAddView) findViewById(R.id.diagnose_view);
		stateLayout = (MeItemLayout) findViewById(R.id.state_layout);
		stateLayout.setOnClickListener(this);
		photoDataLayout = (RelativeLayout) findViewById(R.id.photo_data_layout);
		photoDataLayout.setOnClickListener(this);
		doctorsLayout = (RelativeLayout) findViewById(R.id.doctors_layout);
		doctorsLayout.setOnClickListener(this);
		ruleLayout = (RelativeLayout) findViewById(R.id.rule_layout);
		ruleLayout.setOnClickListener(this);
		abstractLayout = (RelativeLayout) findViewById(R.id.abstract_layout);
		abstractTv = (TextView) findViewById(R.id.abstract_tv);
		abstractEt = (EditText) findViewById(R.id.abstract_et);
		setTextWhacher(AddCaseActivity.this, bedEt, 8000);
	}

	private CaseInfo createCaseInfo(int state) {
		CaseInfo info = new CaseInfo();
		info.name = getName();
		info.sex = selectSex;
		info.age = ageEt.getEditableText().toString();
		info.hos_fullname = getHospitalName();
		info.department = keshiEt.getEditableText().toString();
		info.bed_no = bedEt.getEditableText().toString();
		//
		info.diagnose = createDiagnose();//
		info.ward_no = roomEt.getEditableText().toString();
		info.treat_state = treat_state;
		info.procedure_title = mFlowInfo.title;
		info.procedure_text = mFlowInfo.procedure;
		info.abs = abstractEt.getEditableText().toString();
		info.participate_doctor = currentDoctorList;
		info.state = state;
		return info;
	}
	

	private String getHospitalName() {
		String hostv = (String) hospitalTv.getText();
		if (TextUtils.isEmpty(hostv)) {
			hostv = UserContext.getInstance().getUser().hos_fullname;
		}
		return hostv;
	}

	private void onJumpAction(final int targetId) {
		if (targetId == R.id.photo_data_layout) {
			PhotoDataUploadActivity.show(getActivity(), tempCaseId);
		} else if (targetId == R.id.doctors_layout) {
			CaseInfo caseinfo = new CaseInfo();
			caseinfo.id = tempCaseId;
			caseinfo.admin_doctor = new DoctorInfo(UserContext.getInstance()
					.getUser());
			caseinfo.participate_doctor = currentDoctorList;
			GroupDetailsActivity.show(getActivity(), caseinfo,
					QjConstant.REQUEST_CODE_NEW_CASE_DOCTOR);
			// DoctorPickActivity.show(getActivity(), caseinfo,
			// currentDoctorList, QjConstant.REQUEST_CODE_NEW_CASE);
		}
	}

	protected void save(final int state, final int targetId) {

		CaseInfo info = createCaseInfo(state);
		QjHttp.createpatient(info, new QjHttpCallback<MIdData>() {

			@Override
			public void onError(Call call, Exception e) {
				Toast.makeText(getActivity(), "新病例创建失败 " + e.getMessage(),
						Toast.LENGTH_SHORT).show();

			}

			@Override
			public void onResponseSucces(MIdData response) {
				// TODO Auto-generated method stub
				if (response.data != null) {
					if (state == 1) {
						tempCaseId = response.data.id;
						onJumpAction(targetId);
						// PhotoDataManagerActivity.show(getActivity());
					} else {
						Toast.makeText(getActivity(), "新病例创建成功",
								Toast.LENGTH_SHORT).show();
						EventBus.getDefault().post(
								new CaseEvent(CaseEvent.TYPE_ADD));
						finish();
					}
				} else {
					Toast.makeText(getActivity(), "新临时病例创建失败 ",
							Toast.LENGTH_SHORT).show();

				}
			}

			@Override
			public MIdData parseNetworkResponse(String str) throws Exception {
				return new Gson().fromJson(str, MIdData.class);
			}
		});
	}

	protected void jumpActivity(final int state, final int targetId) {
		if (TextUtils.isEmpty(tempCaseId)) {
			save(state, targetId);
		} else {
			onJumpAction(targetId);
		}
	}

	private String createDiagnose() {
		return diagnose_view.getDiagnoseString();

	}

	private String getName() {
		String str = nameEt.getEditableText().toString();
		if (TextUtils.isEmpty(str)) {
			return "新病人"+ ((((int)(Math.random()*10))%9) +1);
		}
		return str;
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, AddCaseActivity.class);
		context.startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gender_man_tv:
			genderWomanTv.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.btn_choice_nor),
					null, null, null);
			genderManTv
					.setCompoundDrawablesWithIntrinsicBounds(getResources()
							.getDrawable(R.drawable.btn_choice_press), null,
							null, null);
			selectSex = 1;
			break;
		case R.id.gender_woman_tv:
			genderManTv.setCompoundDrawablesWithIntrinsicBounds(getResources()
					.getDrawable(R.drawable.btn_choice_nor), null, null, null);
			genderWomanTv.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.btn_choice_press),
					null, null, null);
			selectSex = 2;
			break;
		case R.id.photo_data_layout:
			jumpActivity(1, R.id.photo_data_layout);
			break;
		case R.id.doctors_layout:
			jumpActivity(1, R.id.doctors_layout);
			break;
		case R.id.hospital_layout:
			MeHospitalActivity.show(getActivity(), currentHospital);
			break;

		case R.id.state_layout:
			EditCaseStateActivity.show(getActivity(), treat_state);
			break;

		case R.id.rule_layout:
			// if(TextUtils.isEmpty(mFlowInfo.title)) {
			// MeFlowActivity.show(getActivity());
			// } else {
			CaseFlowDetailActivity.show(getActivity(), "", mFlowInfo,
					QjConstant.REQUEST_CODE_NEW_CASE_FLOW);
			// MeFlowEditActivity.show(getActivity(), mFlowInfo,
			// QjConstant.REQUEST_CODE_NEW_CASE_FLOW);
			// }
			break;
		}
	}

	private FlowInfo mFlowInfo = new FlowInfo();

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case QjConstant.REQUEST_CODE_NEW_CASE:// 添加群成员
				currentDoctorList = data
						.getParcelableArrayListExtra("selectedDoctorList");
				break;
			case QjConstant.REQUEST_CODE_ADD_HOSPITAL:
				currentHospital = data.getStringExtra("string");
				hospitalTv.setText(currentHospital);
				break;
			case EditCaseActivity.REQUEST_CASE_STATE:
				treat_state = data.getStringExtra("string");
				stateLayout.setRightText(treat_state);
				break;
			case QjConstant.REQUEST_CODE_NEW_CASE_FLOW:
				mFlowInfo = data.getParcelableExtra("FlowInfo");
				break;
			case QjConstant.REQUEST_CODE_NEW_CASE_DOCTOR:
				CaseInfo tempCase = data.getParcelableExtra("caseInfo");
				if (tempCase != null) {
					currentDoctorList = new ArrayList<>(
							tempCase.participate_doctor);
				}
				break;
			}
		}
	}
}
