package com.rmtech.qjys.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.sjl.lib.utils.ScreenUtil;

@SuppressLint("NewApi")
public class MeItemLayout extends RelativeLayout implements
		View.OnClickListener {

	public LinearLayout v_bottom_line;
	public TextView tv_left;
	public TextView tv_right;
	public ImageView iv_right;
	public View inflate;

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
	
	public void setQRcodeRightDrawble(){
		Drawable drawable = getResources().getDrawable(R.drawable.ic_me_qr_code);
		tv_right.setBackgroundDrawable(drawable);
	}
	
	public void setRightTextColor_c3264aa(Context context){
		tv_right.setTextColor(context.getResources().getColor(R.color.c3264aa));
	}

	private void initView(Context context, AttributeSet attrs,
			int defStyleAttr, int defStyleRes) {
		inflate = View.inflate(getContext(), R.layout.qj_meitem_view, this);
		tv_left = (TextView) inflate.findViewById(R.id.tv_left);
		tv_right = (TextView) inflate.findViewById(R.id.tv_right);
		iv_right=(ImageView) inflate.findViewById(R.id.imageView1);
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
			case R.styleable.MeItemView_imageRecource:
				iv_right.setBackground(a.getDrawable(attr));
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
	
	public void setRightGone(){
		this.iv_right.setVisibility(View.INVISIBLE);
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
