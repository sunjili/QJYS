package com.rmtech.qjys.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.sjl.lib.utils.ScreenUtil;

@SuppressLint("NewApi")
public class MeItemLayout extends RelativeLayout implements
		View.OnClickListener {

	private LinearLayout v_bottom_line;
	private TextView tv_left;
	private TextView tv_right;
	private View inflate;

	public MeItemLayout(Context context) {
		super(context);
		initView(context, null, 0, 0);

	}

	public MeItemLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context, attrs, 0, 0);
	}

	public MeItemLayout(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView(context, attrs, defStyleAttr, defStyleRes);
	}

	public MeItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context, attrs, defStyleAttr, 0);

	}

	private void initView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		inflate = View.inflate(getContext(), R.layout.qj_meitem_view, this);
		tv_left = (TextView) inflate.findViewById(R.id.tv_left);
		tv_right = (TextView) inflate.findViewById(R.id.tv_right);
		v_bottom_line = (LinearLayout) inflate
				.findViewById(R.id.ll_bottom_line);
		TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
				R.styleable.MeItemView, defStyleAttr, defStyleRes);
		int n = a.getIndexCount();
		for (int i = 0; i < n; i++) {
			int attr = a.getIndex(i);
			switch (attr) {
			case R.styleable.MeItemView_leftText:
				tv_left.setText(a.getString(attr));
				break;
			case R.styleable.MeItemView_rightText:
				tv_right.setText(a.getString(attr));
				break;
			case R.styleable.MeItemView_rightTextColor:
				tv_right.setTextColor(a.getColor(attr, Color.rgb(126, 126, 126)));
				break;
			case R.styleable.MeItemView_visibleLinePadding:
				boolean visibleLinePadding = a.getBoolean(attr, true);
				if(visibleLinePadding){
					int dp2px = ScreenUtil.dp2px(10);
					v_bottom_line.setPadding(dp2px, 0, dp2px, 0);
				}
			}
		}
		a.recycle();

	}

	@Override
	public void onClick(View v) {
		Toast.makeText(getContext(), "11111111", Toast.LENGTH_LONG).show();
	}
	public void setRightText(String str){
		if(TextUtils.isEmpty(str)){
			tv_right.setText("未设置");
			tv_right.setTextColor(Color.rgb(244, 113, 75));
		}else{
			tv_right.setText(str);
			tv_right.setTextColor(Color.rgb(126, 126, 126));
		}
	}
	public String getRightText(){
		return tv_right.getText().toString();
	}
	
}
