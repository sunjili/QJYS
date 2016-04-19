package com.rmtech.qjys.ui.view;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rmtech.qjys.R;
import com.rmtech.qjys.event.ImageUploadEvent;
import com.sjl.lib.utils.L;

@SuppressLint("NewApi")
public class UploadingView extends RelativeLayout {

	private TextView unread_msg_number;

	public UploadingView(Context context) {
		this(context, null);
	}

	public UploadingView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public UploadingView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public UploadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		initView();
	}

	private void initView() {
		View.inflate(getContext(), R.layout.bview_uploading, this);

		unread_msg_number = (TextView) findViewById(R.id.unread_msg_number);

		this.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		EventBus.getDefault().register(this);

	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		EventBus.getDefault().unregister(this);

	}

	@Subscribe
	public void onEvent(ImageUploadEvent event) {
		if (!this.isAttachedToWindow()) {
			return;
		}
		L.d("onEvent " + event.uploadingNumber);
		if (event.uploadingNumber > 0) {
			this.setVisibility(View.VISIBLE);
			unread_msg_number.setText("" + event.uploadingNumber);
		} else {
			this.setVisibility(View.GONE);
		}

	}

}
