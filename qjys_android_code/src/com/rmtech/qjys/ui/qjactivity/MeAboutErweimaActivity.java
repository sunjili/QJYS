package com.rmtech.qjys.ui.qjactivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.Map;

import com.rmtech.qjys.R;
import com.rmtech.qjys.ui.BaseActivity;
import com.rmtech.qjys.ui.view.SharePopWindow;
import com.sjl.lib.alertview.OnItemClickListener;
import com.tencent.map.b.m;
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
import com.umeng.socialize.utils.BitmapUtils;
import com.umeng.socialize.utils.Log;
import com.umeng.socialize.view.UMFriendListener;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
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
	private ImageView iv_qrcode;
	private RelativeLayout share_view;
	private String qrImageUrl;
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.qj_me_about_erweima);
		setTitle("奇迹医生二维码");
		setLeftTitle("返回");
		initView();
		initUMService();
		context = MeAboutErweimaActivity.this;
		setRightTitle(R.drawable.btn_me_share, new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO 分享
				
				SharePopWindow sharePopWindow = new SharePopWindow(context, displaylist, mItemClickListener);
				sharePopWindow.setTouchable(true);
				sharePopWindow.showAtLocation(share_view, Gravity.BOTTOM, 0, 0);
				sharePopWindow.setBackgroundAlpha(0.5f);
			}
		});
	}
	
	public AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			final int posi = position;
			if(posi != displaylist.length){
				if (mShareAPI.isInstall(MeAboutErweimaActivity.this, displaylist[posi])){
					if(!(displaylist[posi].equals(SHARE_MEDIA.WEIXIN)||displaylist[posi].equals(SHARE_MEDIA.WEIXIN_CIRCLE))){
						mShareAPI.doOauthVerify(MeAboutErweimaActivity.this, displaylist[posi], umAuthListener);
						mShareAPI.getPlatformInfo(MeAboutErweimaActivity.this, displaylist[posi], umAuthListener);
						mShareAPI.getFriend(MeAboutErweimaActivity.this, displaylist[posi], umGetfriendListener);
					}
					
					new ShareAction(getActivity()).setPlatform(displaylist[posi]).withText("奇迹医生是一款专业服务于临床医生的严肃协作平台。").withTitle("奇迹医生-连接医生，创造奇迹。")
							.withTargetUrl("http://m.qijiyisheng.com/").withMedia(image).setCallback(umShareListener)
							.share();
				}else {
					// 将二维码压缩存到本地
					try {
						String filePath = Environment.getExternalStorageDirectory() + "/qjys" + File.separator + "qrQJYSPic" + ".jpg";
					    iv_qrcode.setDrawingCacheEnabled(true);
					    Bitmap bitmap = Bitmap.createBitmap(iv_qrcode.getDrawingCache());
					    iv_qrcode.setDrawingCacheEnabled(false);
						bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}else {
				
			}
			
		}
		
	};

	public void initUMService() {

		mShareAPI = UMShareAPI.get(this);

		Config.isloadUrl = true;
		Config.OpenEditor = false;
		Config.IsToastTip = true;
		ProgressDialog dialog =  new ProgressDialog(this);
        dialog.setMessage("跳转中，请稍候…");
        Config.dialog = dialog;
		image = new UMImage(MeAboutErweimaActivity.this,
                BitmapFactory.decodeResource(getResources(), R.drawable.ic_me_logo));
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
		iv_qrcode = (ImageView) findViewById(R.id.iv_qrcode);
		share_view = (RelativeLayout) findViewById(R.id.share_view);
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
