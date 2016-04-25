package com.rmtech.qjys.ui.view;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.GridView;

@SuppressLint("NewApi")
public class ImageLoaderGridView extends GridView implements OnScrollListener {

	private OnScrollListener mExtraOnScrollListener;

	public ImageLoaderGridView(Context context) {
		super(context);
		init();
	}

	public ImageLoaderGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ImageLoaderGridView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	@SuppressLint("NewApi")
	private void init() {
		super.setOnScrollListener(this);
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		mExtraOnScrollListener = l;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if (mExtraOnScrollListener != null) {
			mExtraOnScrollListener.onScrollStateChanged(view, scrollState);
		}
		if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_FLING) {
			ImageLoader.getInstance().resume();
		} else {
			ImageLoader.getInstance().pause();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		if (mExtraOnScrollListener != null) {
			mExtraOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

}
