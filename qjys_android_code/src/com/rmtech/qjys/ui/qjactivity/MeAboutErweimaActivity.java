package com.rmtech.qjys.ui.qjactivity;

import java.util.Map;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.editorpage.ShareActivity;
import com.umeng.socialize.handler.UMAPIShareHandler;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.view.UMFriendListener;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

/***
 * 二维码 页面
 * 
 * @author Administrator
 * 
 */
public class MeAboutErweimaActivity extends BaseActivity {
	private EditText et_name;
	private String name;
	private Context context;
	private UMShareAPI mShareAPI;
	private UMImage image;
	private SHARE_MEDIA[] displaylist;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_about_erweima);
		setTitle("奇迹医生二维码");
		setLeftTitle("返回");
		initUMService();
		context = MeAboutErweimaActivity.this;
		setRightTitle(R.drawable.btn_me_share, new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO 分享
				if (mShareAPI.isInstall(MeAboutErweimaActivity.this, SHARE_MEDIA.WEIXIN)) {
					
					mShareAPI.doOauthVerify(MeAboutErweimaActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
					mShareAPI.getPlatformInfo(MeAboutErweimaActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
					mShareAPI.getFriend(MeAboutErweimaActivity.this, SHARE_MEDIA.WEIXIN, umGetfriendListener);
					
					new ShareAction(getActivity()).setDisplayList(displaylist).withText("呵呵").withTitle("title")
							.withTargetUrl("http://www.zhihu.com").withMedia(image).setListenerList(umShareListener)
							.open();
				}
				// final Dialog dialog = new Dialog(context, R.style.dialog);
				// dialog.setContentView(R.layout.qj_me_dialog);
				// dialog.findViewById(R.id.tv_paizhao).setOnClickListener(
				// new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// dialog.cancel();
				// }
				// });
				// dialog.findViewById(R.id.tv_xiangce).setOnClickListener(
				// new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// dialog.cancel();
				// }
				// });
				// dialog.findViewById(R.id.tv_guanliqi).setOnClickListener(
				// new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// dialog.cancel();
				// }
				// });
				//
				// dialog.findViewById(R.id.tv_cancle).setOnClickListener(
				// new OnClickListener() {
				//
				// @Override
				// public void onClick(View v) {
				// dialog.cancel();
				// }
				// });
				// dialog.setCancelable(true);
				// dialog.create();
				// dialog.show();
			}
		});
		initView();
	}

	public void initUMService() {

		mShareAPI = UMShareAPI.get(this);

		Config.isloadUrl = true;
		Config.OpenEditor = false;
		Config.IsToastTip = true;
		image = new UMImage(MeAboutErweimaActivity.this, "http://www.umeng.com/images/pic/social/integrated_3.png");
		displaylist = new SHARE_MEDIA[] { SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE };
	}

	private UMFriendListener umGetfriendListener = new UMFriendListener() {

		@Override
		public void onError(SHARE_MEDIA arg0, int arg1, Throwable arg2) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Getfriend onError", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onComplete(SHARE_MEDIA arg0, int arg1, Map<String, Object> arg2) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Getfriend succeed", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA arg0, int arg1) {
			// TODO Auto-generated method stub
			Toast.makeText(getApplicationContext(), "Getfriend onCancel", Toast.LENGTH_SHORT).show();
		}
	};

	private UMAuthListener umAuthListener = new UMAuthListener() {
		@Override
		public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
			Toast.makeText(getApplicationContext(), "Authorize succeed", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(SHARE_MEDIA platform, int action, Throwable t) {
			Toast.makeText(getApplicationContext(), "Authorize fail", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform, int action) {
			Toast.makeText(getApplicationContext(), "Authorize cancel", Toast.LENGTH_SHORT).show();
		}
	};

	UMShareListener umShareListener = new UMShareListener() {
		@Override
		public void onResult(SHARE_MEDIA platform) {
			Toast.makeText(MeAboutErweimaActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onError(SHARE_MEDIA platform, Throwable t) {
			Toast.makeText(MeAboutErweimaActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
		}

		@Override
		public void onCancel(SHARE_MEDIA platform) {
			Toast.makeText(MeAboutErweimaActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
		}
	};

	private void initView() {

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mShareAPI.onActivityResult(requestCode, resultCode, data);
	}

	protected boolean showTitleBar() {
		return true;
	}

	public static void show(Context context) {
		Intent intent = new Intent();
		intent.setClass(context, MeAboutErweimaActivity.class);
		context.startActivity(intent);
	}
}
