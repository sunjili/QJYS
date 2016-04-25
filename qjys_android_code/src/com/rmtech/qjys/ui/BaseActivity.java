package com.rmtech.qjys.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;

import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.view.TopTitleView;

public class BaseActivity extends EaseBaseActivity {

	private ViewGroup mRootLayout;

	protected TopTitleView mTopTitleView;

	private String mTitle;
	private static final String TITLE_KEY = "TITLE_KEY";

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		
	}

	protected boolean showTitleBar() {
		return false;
	}

	protected BaseActivity getActivity() {
		return BaseActivity.this;
	}

	
	@Override
    public void startActivity(Intent intent) {
    	if(!TextUtils.isEmpty(mTitle)) {
    		intent.putExtra(TITLE_KEY, mTitle);
    	}
        super.startActivity(intent);
    }

	@Override
	public void setContentView(int layoutResID) {
		if (showTitleBar()) {
			mRootLayout = (ViewGroup) View.inflate(this, R.layout.activity_base, null);
			View child = View.inflate(this, layoutResID, null);
			if (mRootLayout == null) {
				super.setContentView(layoutResID);
				return;
			}
			mTopTitleView = (TopTitleView) mRootLayout.findViewById(R.id.title_view);
			mRootLayout.addView(child, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

			Intent intent = getIntent();
			String leftTitle = null;
			if (intent != null) {
				leftTitle = intent.getStringExtra(TITLE_KEY);
				if (!TextUtils.isEmpty(leftTitle) && showTitleBar()) {
					if (mTopTitleView != null) {
						mTopTitleView.setLeftView(leftTitle);
					}
				}

			}
			super.setContentView(mRootLayout);
		} else {
			super.setContentView(layoutResID);
		}
		
		
	}

	protected void setTitle(String title) {
		mTitle = title;
		if (mTopTitleView != null) {
			mTopTitleView.setTitle(title);
		}
	}
	
	protected void setLeftTitle(String leftTitle) {
		if (mTopTitleView != null) {
			mTopTitleView.setLeftView(leftTitle);
		}
	}

	protected View setRightTitle(String title,View.OnClickListener listener) {
		if (mTopTitleView != null) {
			return mTopTitleView.setRightTitle(title, listener);
		}
		return null;
	}
	
	protected View addRightIcon(int drawRId,View.OnClickListener listener) {
		if (mTopTitleView != null) {
			return mTopTitleView.addRightIcon(drawRId, listener);
		}
		return null;
	}
	
	protected View setRightTitle(int drawRId,View.OnClickListener listener) {
		if (mTopTitleView != null) {
			return mTopTitleView.setRightTitle(drawRId, listener);
		}
		return null;
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		// umeng
		// MobclickAgent.onResume(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		// umeng
		// MobclickAgent.onPause(this);
	}

}
