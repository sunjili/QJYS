package com.rmtech.qjys.ui.view;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.rmtech.qjys.R;

@SuppressLint("NewApi")
public class DiagnoseAddView extends RelativeLayout implements
		View.OnClickListener {

	private LinearLayout mRootView;

	private ArrayList<EditText> editViewList = new ArrayList<EditText>();

	public DiagnoseAddView(Context context) {
		this(context, null);
	}

	public DiagnoseAddView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DiagnoseAddView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public DiagnoseAddView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView();
	}

	private void initView() {
		mRootView = (LinearLayout) View.inflate(getContext(),
				R.layout.bview_diagnose_add, null);
		this.addView(mRootView);
		findViewById(R.id.diagnose_image).setOnClickListener(this);
		editViewList.add((EditText) findViewById(R.id.diagnose_et));
		editViewList.add((EditText) findViewById(R.id.diagnose_et2));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.diagnose_image:
			if(mRootView.getChildCount()>10){
				Toast.makeText(getContext(), "您不能添加更多的诊断！", Toast.LENGTH_SHORT).show();
				return;
			}
			View addView = View.inflate(getContext(),
					R.layout.bview_diagnose_add_item, null);
			editViewList.add((EditText) addView.findViewById(R.id.diagnose_et1));
			String string = ((EditText) findViewById(R.id.diagnose_et2)).getText().toString();
			((EditText) addView.findViewById(R.id.diagnose_et1)).setText(string);
			((EditText) findViewById(R.id.diagnose_et2)).setText("");
			int index = mRootView.getChildCount()-1;
			mRootView.addView(addView, index);
			break;
		}

	}

	public String getDiagnoseString() {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < editViewList.size(); i++) {
			String str = editViewList.get(i).getText().toString();
			if (TextUtils.isEmpty(str)) {
				continue;
			}
			sb.append(str);
			sb.append("&&");
		}
		if (sb.length() > 2) {
			return sb.toString().substring(0, sb.length() - 2);
		} else {
			return "";
		}
	}

}
