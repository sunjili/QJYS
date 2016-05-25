package com.rmtech.qjys.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rmtech.qjys.R;

public class TopTitleView extends FrameLayout {

	/**
	 * 最左返回键
	 */
	private TextView mLeftView;
	private TextView mTitleTextView;
	private TextView mRightView;
	private ImageView mBackImage;
	private TextView sub_right_title;

	public static final int ACTION_BAR_LEFT = 0;
	public static final int ACTION_BAR_RIGHT = 1;
	public static final int ACTION_BAR_MIDDLE = 2;

	public TopTitleView(Context context) {
		this(context, null);
	}

	public TopTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {

	}

	@Override
	protected void onFinishInflate() {
		setDrawingCacheEnabled(true);
		setAlwaysDrawnWithCacheEnabled(true);
		mBackImage = (ImageView) findViewById(R.id.backImage);
		mLeftView = (TextView) findViewById(R.id.left_title);
		mTitleTextView = (TextView) findViewById(R.id.title);
		mRightView = (TextView) findViewById(R.id.right_title);
		sub_right_title = (TextView) findViewById(R.id.sub_right_title);
	}

	public TextView setTitle(String title) {
		if (mTitleTextView != null) {
			mTitleTextView.setText(title);
		}
		return mTitleTextView;
	}
	
	public View getBackImage(){
		return mBackImage;
	}

	public void setTitle(String title, String extra) {
		if (mTitleTextView != null) {
			mTitleTextView.setText(title);
		}
		if (!TextUtils.isEmpty(extra)) {
			mRightView.setText(extra);
			mRightView.setVisibility(View.VISIBLE);
		}
	}

	public View setRightTitle(String title, View.OnClickListener listener) {
		if (mRightView != null) {
			mRightView.setText(title);
			mRightView.setVisibility(View.VISIBLE);
			setView(mRightView, 0, listener);
		}
		return mRightView;
	}

	public View setRightTitle(int drawRid, View.OnClickListener listener) {
		if (mRightView != null) {
			mRightView.setText("");
			mRightView.setVisibility(View.VISIBLE);
			setView(mRightView, drawRid, listener);
		}
		return mRightView;
	}

	public void setLeftTitle(String title, View.OnClickListener listener) {
		if (mLeftView != null) {
			mLeftView.setText(title);
			mLeftView.setVisibility(View.VISIBLE);
			setView(mLeftView, 0, listener);
		}
	}

	public void setTitle(String title, View.OnClickListener listener) {
		if (mTitleTextView != null) {
			mTitleTextView.setText(title);
			setView(mTitleTextView, 0, listener);
		}
	}

	public View setLeftView(int backgroudResId, String name, View.OnClickListener listener) {
		if (mLeftView != null) {
			mLeftView.setVisibility(VISIBLE);
			if (backgroudResId != 0) {
				mLeftView.setBackgroundResource(backgroudResId);
			}
			if (listener != null) {
				mLeftView.setOnClickListener(listener);
			}
//			if (!TextUtils.isEmpty(name)) {
				mLeftView.setText(name);
//			}
		}
		return mLeftView;
	}

	public View setLeftView(String name) {
		return setLeftView(0, name, null);
	}

	private void setView(View view, int backgroudResId, View.OnClickListener listener) {
		if (view != null) {
			view.setVisibility(View.VISIBLE);
			if (backgroudResId != 0) {
				view.setBackgroundResource(backgroudResId);
			}
			if (listener != null) {
				view.setOnClickListener(listener);
			}
		}
	}

	public void setOnBackClickLitener(OnClickListener onClickListener) {
		mLeftView.setOnClickListener(onClickListener);
	}

	public View addRightIcon(int drawRId, OnClickListener listener) {
		setView(sub_right_title, drawRId, listener);
		return sub_right_title;
	}

}
