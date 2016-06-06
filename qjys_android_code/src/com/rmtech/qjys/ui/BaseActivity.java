package com.rmtech.qjys.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

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

	public void setTextWhacher(final Context context, final EditText mEditText, final int maxLength){
		
		
		mEditText.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;  
	        private int editStart ;  
	        private int editEnd ;  
		    @Override
		    public void onTextChanged(CharSequence s, int start, int before, int count) {
		        // TODO Auto-generated method stub
		    	temp = s;  
		    }
		    
		    @Override
		    public void beforeTextChanged(CharSequence s, int start, int count,
		            int after) {
		        // TODO Auto-generated method stub
		        
		    }
		    
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				editStart = mEditText.getSelectionStart();  
	            editEnd = mEditText.getSelectionEnd();
	            try {
					if (temp.toString().getBytes("utf-8").length > maxLength) {  
					    Toast.makeText(context,  
					            "你输入的字数已经超过了限制！", Toast.LENGTH_SHORT)  
					            .show();  
					    s.delete(editStart-1, editEnd);  
					    int tempSelection = editStart;  
					    mEditText.setText(s);  
					    mEditText.setSelection(tempSelection);  
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}
		});
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

	protected TextView setTitle(String title) {
		mTitle = title;
		if (mTopTitleView != null) {
			return mTopTitleView.setTitle(title);
		}
		return null;
	}
	
	protected void setLeftGone() {
		if (mTopTitleView != null) {
			mTopTitleView.getChildAt(0).setVisibility(View.GONE);
		}
	}
	
	protected void setBackImageGone() {
		if (mTopTitleView != null) {
			mTopTitleView.getBackImage().setVisibility(View.GONE);
		}
	}
	
	protected void setLeftTitle(String leftTitle) {
		if (mTopTitleView != null) {
			mTopTitleView.setLeftTitle(leftTitle, null);
		}
	}

	protected TextView setRightTitle(String title,View.OnClickListener listener) {
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
	
	protected View setRightIcon(int drawRId,View.OnClickListener listener) {
		if (mTopTitleView != null) {
			return mTopTitleView.setRightIcon(drawRId, listener);
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
