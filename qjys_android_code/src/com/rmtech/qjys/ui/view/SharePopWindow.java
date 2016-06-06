package com.rmtech.qjys.ui.view;

import com.rmtech.qjys.R;
import com.umeng.socialize.bean.SHARE_MEDIA;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("NewApi")
public class SharePopWindow extends PopupWindow{
	
	public GridView gridView;
	public Context context;
	public TextView share_cancel;
	
	
	public SharePopWindow(final Context context, final SHARE_MEDIA[] list, AdapterView.OnItemClickListener listener) {
		// TODO Auto-generated constructor stub
		this.context = context;
		 // 一个自定义的布局，作为显示的内容
		LayoutInflater inflater = (LayoutInflater) context  
	                .getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
        View contentView = inflater.inflate(R.layout.qj_share_popwindow, null);
        this.setContentView(contentView);
        this.setWidth(((Activity)context).getWindowManager().getDefaultDisplay().getWidth());
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //点击空白处时，隐藏掉pop窗口  
        this.setFocusable(true);  
        this.setBackgroundDrawable(new BitmapDrawable());  
        
        this.setOnDismissListener(new poponDismissListener()); 
        
        gridView = (GridView) contentView.findViewById(R.id.grid_share);
        ShareGridAdapter adapter = new ShareGridAdapter(context, list,listener);
		gridView.setAdapter(adapter);
		share_cancel = (TextView) contentView.findViewById(R.id.share_cancel);
		share_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SharePopWindow.this.dismiss();
			}
		});
		this.setAnimationStyle(R.anim.slide_in_bottom);
//        gridView.setOnItemClickListener();
	}
	
	/** 
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来 
     * @author 
     * 
     */  
    class poponDismissListener implements PopupWindow.OnDismissListener{  
  
        @Override  
        public void onDismiss() {  
            // TODO Auto-generated method stub  
            setBackgroundAlpha(1f);  
        }  
          
    } 
	
	/** 
     * 设置添加屏幕的背景透明度 
     * @param bgAlpha 
     */  
    public void setBackgroundAlpha(float bgAlpha) {  
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();  
        lp.alpha = bgAlpha; //0.0-1.0  
        ((Activity) context).getWindow().setAttributes(lp);  
    } 
	
	public class ShareGridAdapter extends BaseAdapter {
		
		private SHARE_MEDIA[] list;
		private Context context;
		private OnItemClickListener listener;
		

		public ShareGridAdapter(Context context, SHARE_MEDIA[] list,OnItemClickListener listener) {
			super();
			// TODO Auto-generated constructor stub
			this.list = list;
			this.context = context;
			this.listener = listener;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.length + 1;
		}

		@Override
		public SHARE_MEDIA getItem(int position) {
			// TODO Auto-generated method stub
			return list[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position,  View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ViewHolder holder;
			final int posi = position;
			
			if(convertView == null){
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.qj_share_item, null);
				holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
				holder.textView = (TextView) convertView.findViewById(R.id.textView);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			final View view = convertView;
			if(posi == list.length){
				holder.imageView.setImageResource(R.drawable.btn_me_save_image);
				holder.textView.setText("保存图片");
			}else if(posi < list.length){
				if(list[position].equals(SHARE_MEDIA.WEIXIN)){
					holder.imageView.setImageResource(R.drawable.btn_me_wechat);
					holder.textView.setText("微信好友");
				}else if(list[position].equals(SHARE_MEDIA.WEIXIN_CIRCLE)){
					holder.imageView.setImageResource(R.drawable.btn_me_circle_of_friends);
					holder.textView.setText("微信朋友圈");
				}
			}
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(listener != null) {
						listener.onItemClick(null,view ,position,0);
					}
				}
			});
			return convertView;
		}
		
		private class ViewHolder {
			ImageView imageView;
			TextView textView;
		}
		
	}

}
