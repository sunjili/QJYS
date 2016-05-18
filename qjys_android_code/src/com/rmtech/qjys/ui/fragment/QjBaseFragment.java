package com.rmtech.qjys.ui.fragment;

import android.content.Intent;

import com.hyphenate.easeui.ui.EaseBaseFragment;

public abstract class QjBaseFragment extends EaseBaseFragment {

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

}
