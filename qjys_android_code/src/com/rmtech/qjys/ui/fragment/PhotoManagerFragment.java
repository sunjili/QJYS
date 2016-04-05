package com.rmtech.qjys.ui.fragment;

import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Toast;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.view.CaseTopBarView;
import com.sjl.lib.dynamicgrid.DynamicGridView;
import com.sjl.lib.dynamicgrid.example.CheeseDynamicAdapter;
import com.sjl.lib.dynamicgrid.example.Cheeses;

public class PhotoManagerFragment extends QjBaseFragment {

	protected static final String TAG = PhotoManagerFragment.class.getSimpleName();
	private DynamicGridView mGridView;
	
	private CaseTopBarView mQuickReturnView;
    private int mCachedVerticalScrollRange;
    private int mQuickReturnHeight;

    private static final int STATE_ONSCREEN = 0;
    private static final int STATE_OFFSCREEN = 1;
    private static final int STATE_RETURNING = 2;
    private int mState = STATE_ONSCREEN;
    private int mScrollY;
    private int mMinRawY = 0;
    private TranslateAnimation anim;


	public PhotoManagerFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.bfragment_photo_manager, container, false);
		return rootView;
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		mGridView = (DynamicGridView) getView().findViewById(R.id.dynamic_grid);

	}

	public boolean onBackPressed() {
		if (mGridView.isEditMode()) {
			mGridView.stopEditMode();
			return true;
		}
		return false;
	}

	@Override
	protected void setUpView() {
		// TODO Auto-generated method stub
		mGridView.setAdapter(new CheeseDynamicAdapter(getActivity(), new ArrayList<String>(Arrays
				.asList(Cheeses.sCheeseStrings)), getResources().getInteger(R.integer.column_count)));
		// add callback to stop edit mode if needed
		// gridView.setOnDropListener(new DynamicGridView.OnDropListener()
		// {
		// @Override
		// public void onActionDrop()
		// {
		// gridView.stopEditMode();
		// }
		// });
		mGridView.setOnDragListener(new DynamicGridView.OnDragListener() {
			@Override
			public void onDragStarted(int position) {
				Log.d(TAG, "drag started at position " + position);
			}

			@Override
			public void onDragPositionsChanged(int oldPosition, int newPosition) {
				Log.d(TAG, String.format("drag item position changed from %d to %d", oldPosition, newPosition));
			}
		});
		mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				mGridView.startEditMode(position);
				return true;
			}
		});

		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(getActivity(), parent.getAdapter().getItem(position).toString(), Toast.LENGTH_SHORT)
						.show();
			}
		});
		
		mGridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        mQuickReturnHeight = mQuickReturnView.getHeight();
                        mGridView.computeScrollY();
                        mCachedVerticalScrollRange = mGridView.getListHeight();
                    }
                });

        mGridView.setOnScrollListener(new OnScrollListener() {
            @SuppressLint("NewApi")
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {

                mScrollY = 0;
                int translationY = 0;

                if (mGridView.scrollYIsComputed()) {
                    mScrollY = mGridView.getComputedScrollY();
                }

                int rawY = mGridView.getTop()
                        - Math.min(mCachedVerticalScrollRange - mGridView.getHeight(), mScrollY);

                switch (mState) {
                case STATE_OFFSCREEN:
                    if (rawY <= mMinRawY) {
                        mMinRawY = rawY;
                    } else {
                        mState = STATE_RETURNING;
                    }
                    translationY = rawY;
                    break;

                case STATE_ONSCREEN:
                    if (rawY < -mQuickReturnHeight) {
                        mState = STATE_OFFSCREEN;
                        mMinRawY = rawY;
                    }
                    translationY = rawY;
                    break;

                case STATE_RETURNING:
                    translationY = (rawY - mMinRawY) - mQuickReturnHeight;
                    if (translationY > 0) {
                        translationY = 0;
                        mMinRawY = rawY - mQuickReturnHeight;
                    }

                    if (rawY > 0) {
                        mState = STATE_ONSCREEN;
                        translationY = rawY;
                    }

                    if (translationY < -mQuickReturnHeight) {
                        mState = STATE_OFFSCREEN;
                        mMinRawY = rawY;
                    }
                    break;
                }

                /** this can be used if the build is below honeycomb **/
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
                    anim = new TranslateAnimation(0, 0, translationY,
                            translationY);
                    anim.setFillAfter(true);
                    anim.setDuration(0);
                    mQuickReturnView.startAnimation(anim);
                } else {
                    mQuickReturnView.setTranslationY(translationY);
                }

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
        });
	}

	public void setQuickReturnView(CaseTopBarView mCaseTopBarView) {
		// TODO Auto-generated method stub
		mQuickReturnView = mCaseTopBarView;
		
	}
}