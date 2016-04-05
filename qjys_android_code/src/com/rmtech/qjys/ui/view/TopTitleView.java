package com.rmtech.qjys.ui.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.rmtech.qjys.R;

public class TopTitleView extends FrameLayout {

	/**
	 * 最左返回键
	 */
	private TextView mLeftView;
	private TextView mTitleTextView;
	private TextView mRightView;

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

		mLeftView = (TextView)findViewById(R.id.left_title);
		mTitleTextView = (TextView) findViewById(R.id.title);
		mRightView = (TextView) findViewById(R.id.right_title);
	}

	public void setTitle(String title) {
		if (mTitleTextView != null) {
			mTitleTextView.setText(title);
		}
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

	public void setRightTitle(String title, View.OnClickListener listener) {
		if (mRightView != null) {
			mRightView.setText(title);
			mRightView.setVisibility(View.VISIBLE);
			setView(mRightView, 0, listener);
		}
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
			if(listener != null) {
				mLeftView.setOnClickListener(listener);
			}
			if(!TextUtils.isEmpty(name)) {
				mLeftView.setText(name);
			}
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

}