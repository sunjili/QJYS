package com.hyphenate.easeui.widget;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Parcel;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.TextAppearanceSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.rmtech.qjys.R;
import com.rmtech.qjys.model.DoctorInfo;

/**
 * 聊天输入栏主菜单栏
 *
 */
public class EaseChatPrimaryMenu extends EaseChatPrimaryMenuBase implements OnClickListener {
    private EditText editText;
    private View buttonSetModeKeyboard;
    private RelativeLayout edittext_layout;
//    private View buttonSetModeVoice;
    private View buttonSend;
//    private View buttonPressToSpeak;
//    private ImageView faceNormal;
//    private ImageView faceChecked;
    private Button buttonMore;
//    private RelativeLayout faceLayout;
    private Context context;
//    private EaseVoiceRecorderView voiceRecorderView;

    public EaseChatPrimaryMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public EaseChatPrimaryMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EaseChatPrimaryMenu(Context context) {
        super(context);
        init(context, null);
    }

	private AtTextAppearanceSpan[] mSpans; // 记录所有的@相关span对象
	private int lastchangedLen = 0;
	private boolean mIsKeyDelPressed = false; // 删除键事件标志，在删除到@的时候，不会弹出选择界面



    private void init(final Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.ease_widget_chat_primary_menu, this);
        editText = (EditText) findViewById(R.id.et_sendmessage);
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
//        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = findViewById(R.id.btn_send);
//        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
//        faceNormal = (ImageView) findViewById(R.id.iv_face_normal);
//        faceChecked = (ImageView) findViewById(R.id.iv_face_checked);
//        faceLayout = (RelativeLayout) findViewById(R.id.rl_face);
        buttonMore = (Button) findViewById(R.id.btn_more);
//        edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_normal);
        
        buttonSend.setOnClickListener(this);
        buttonSetModeKeyboard.setOnClickListener(this);
//        buttonSetModeVoice.setOnClickListener(this);
        buttonMore.setOnClickListener(this);
//        faceLayout.setOnClickListener(this);
        editText.setOnClickListener(this);
        editText.requestFocus();
        
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_active);
                } else {
//                    edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_normal);
                }

            }
        });
        
        editText.setOnKeyListener(mOnEditTextKeyListener);

        // 监听文字框
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    buttonMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    buttonMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        		if (mSpans != null) {
    				// 如果发现在某个span包内进行了编辑，那么就从所有有效span包内移除这个span包
    				for (AtTextAppearanceSpan mspan : mSpans) {
    					if (mspan instanceof AtTextAppearanceSpan) {
    						final Editable editable = editText.getEditableText();
    						int editablestart = editable.getSpanStart(mspan);
    						int editableend = editable.getSpanEnd(mspan);
    						if (start > editablestart && start < editableend) {
    							editText.getText().removeSpan(mspan);
    							removeAtFriend(mspan.getData());
    							break;
    						}
    					}
    				}
    				lastchangedLen = s.length();
    			}
            }

            @Override
            public void afterTextChanged(Editable s) {
            	if (s.length() > 0) {
    				mSpans = editText.getText().getSpans(0, editText.length(), AtTextAppearanceSpan.class);
    				String text = s.toString();
    				if (!mIsKeyDelPressed)
    				{
    					int index = editText.getSelectionStart();
    					String subStr = text.substring(0, index);
    					if (subStr.endsWith("@")) {
    						boolean needToSelect = true;
    						if (text.equals("@")) {
    							if(s.length() < lastchangedLen) { //删除时  最后剩下 @ 也不打开新页面
    								needToSelect = false;
    							}else {
    								needToSelect = true;
    							}

    						} else {
    							int lastIndex = text.lastIndexOf("@");
    							if (lastIndex > 0) { //定位到首位时，说明没有@，跳转
    								String lastChar = text.substring(lastIndex - 1, lastIndex);

    								if (!TextUtils.isEmpty(lastChar)) {
    									Pattern p = Pattern.compile("[0-9]*");
    									Matcher m = p.matcher(lastChar);
    									if (m.matches()) { // 在数字后面输入的@无效
    										needToSelect = false;
    									} else {
    										p = Pattern.compile("[a-zA-Z]"); // 在字母后面输入的@无效，同微信一致
    										m = p.matcher(lastChar);
    										if (m.matches()) {
    											needToSelect = false;
    										}
    									}
    								}

    								if((!TextUtils.isEmpty(lastChar) || TextUtils.isEmpty(lastChar) && lastIndex == 0 ) && TextUtils.equals(lastChar, " ") ) {
    									needToSelect = false;
    								}
    							}
    						}
    						if (needToSelect) {
    							//DoctorPickActivity.show(activity, caseinfo, selectedDoctorList, type);
    							 if(listener != null){
    				                listener.onAtShow();
    				            }
    						}
    					}
    				}
    			}
            }
        });
        
        
//        buttonPressToSpeak.setOnTouchListener(new OnTouchListener() {
//            
//            @Override 
//            public boolean onTouch(View v, MotionEvent event) {
//                if(listener != null){
//                    return listener.onPressToSpeakBtnTouch(v, event);
//                }
//                return false;
//            }
//        });
    }
    
	private void removeAtFriend(DoctorInfo friendNode) {
//		if (mRmdVideoItem != null && !StringUtils.isEmptyStr(mRmdVideoItem.getVid())) {
//			RemindFriendNotes.getInstance().removeAFriend(friendNode, mRmdVideoItem.getVid());
//		}
		mAtMap.add(friendNode);

	}
    
    /**
     * 设置长按说话录制控件
     * @param voiceRecorderView
     */
//    public void setPressToSpeakRecorderView(EaseVoiceRecorderView voiceRecorderView){
//        this.voiceRecorderView = voiceRecorderView;
//    }

    /**
     * 表情输入
     * @param emojiContent
     */
    public void onEmojiconInputEvent(CharSequence emojiContent){
        editText.append(emojiContent);
    }
    
    /**
     * 表情删除
     */
    public void onEmojiconDeleteEvent(){
        if (!TextUtils.isEmpty(editText.getText())) {
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            editText.dispatchKeyEvent(event);
        }
    }
    
    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view){
        int id = view.getId();
        if (id == R.id.btn_send) {
            if(listener != null){
                String s = editText.getText().toString();
                editText.setText("");
                listener.onSendBtnClicked(s);
            }
        	mAtMap.clear();

//        } else if (id == R.id.btn_set_mode_voice) {
//            setModeVoice();
//            showNormalFaceImage();
//            if(listener != null)
//                listener.onToggleVoiceBtnClicked();
        } else if (id == R.id.btn_set_mode_keyboard) {
            setModeKeyboard();
//            showNormalFaceImage();
            if(listener != null)
                listener.onToggleVoiceBtnClicked();
        } else if (id == R.id.btn_more) {
//            buttonSetModeVoice.setVisibility(View.VISIBLE);
            buttonSetModeKeyboard.setVisibility(View.GONE);
            edittext_layout.setVisibility(View.VISIBLE);
//            buttonPressToSpeak.setVisibility(View.GONE);
//            showNormalFaceImage();
            if(listener != null)
                listener.onToggleExtendClicked();
        } else if (id == R.id.et_sendmessage) {
//            edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_active);
//            faceNormal.setVisibility(View.VISIBLE);
//            faceChecked.setVisibility(View.INVISIBLE);
            if(listener != null)
                listener.onEditTextClicked();
//        } else if (id == R.id.rl_face) {
//            toggleFaceImage();
//            if(listener != null){
//                listener.onToggleEmojiconClicked();
//            }
        } else {
        }
    }
    
    
    /**
     * 显示语音图标按钮
     * 
     */
    protected void setModeVoice() {
        hideKeyboard();
        edittext_layout.setVisibility(View.GONE);
//        buttonSetModeVoice.setVisibility(View.GONE);
        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.GONE);
        buttonMore.setVisibility(View.VISIBLE);
//        buttonPressToSpeak.setVisibility(View.VISIBLE);
//        faceNormal.setVisibility(View.VISIBLE);
//        faceChecked.setVisibility(View.INVISIBLE);

    }

    /**
     * 显示键盘图标
     */
    protected void setModeKeyboard() {
        edittext_layout.setVisibility(View.VISIBLE);
        buttonSetModeKeyboard.setVisibility(View.GONE);
//        buttonSetModeVoice.setVisibility(View.VISIBLE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        editText.requestFocus();
        // buttonSend.setVisibility(View.VISIBLE);
//        buttonPressToSpeak.setVisibility(View.GONE);
        if (TextUtils.isEmpty(editText.getText())) {
            buttonMore.setVisibility(View.VISIBLE);
            buttonSend.setVisibility(View.GONE);
        } else {
            buttonMore.setVisibility(View.GONE);
            buttonSend.setVisibility(View.VISIBLE);
        }

    }

	@Override
	public void onExtendMenuContainerHide() {
		// TODO Auto-generated method stub
		
	}
	
	private class AtTextAppearanceSpan extends TextAppearanceSpan{
		private DoctorInfo mData;

		public AtTextAppearanceSpan(Context context, int appearance) {
			super(context, appearance);
		}

		public AtTextAppearanceSpan(Context context, int appearance, int colorList) {
			super(context, appearance, colorList);
		}

		public AtTextAppearanceSpan(String family, int style, int size, ColorStateList color, ColorStateList linkColor) {
			super(family, style, size, color, linkColor);
		}

		public AtTextAppearanceSpan(Context context, DoctorInfo data) {
			super(context, R.style.at_somebody);
			mData = data;
		}

		public AtTextAppearanceSpan(Parcel src) {
			super(src);
		}

		public DoctorInfo getData() {
			return mData;
		}
	}
    
    
	private View.OnKeyListener mOnEditTextKeyListener = new View.OnKeyListener() {
		@Override
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
				if (editText.getText().length() > 0) {
					mIsKeyDelPressed = true;
				}
				int index = editText.getSelectionStart(); // 获取光标的位置
				if (mSpans != null) {
					for (AtTextAppearanceSpan mspan : mSpans) {
						if (mspan instanceof AtTextAppearanceSpan) {
							final Editable editable = editText.getEditableText();
							int start = editable.getSpanStart(mspan);
							int end = editable.getSpanEnd(mspan);
							if (index == end) {
								editable.delete(start + 1, end);
								removeAtFriend(mspan.getData());
								break;
							}
						}
					}
				}
			}else  if(event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_DEL ) {
				mIsKeyDelPressed = false;
			}
			return false;
		}
	};

	public HashSet<DoctorInfo> mAtMap = new HashSet<DoctorInfo> ();
	@Override
	public void onAddAtFriend(DoctorInfo data) {
		int index = editText.getSelectionStart();
		Editable editable = editText.getText();
		String appendStr = data.getDisplayName() + " ";
		if(index == 0) {
			appendStr = "@"+appendStr;
		} else if (index > 0){
			if(!"@".equalsIgnoreCase(editText.getText().toString().substring(index-1, index))) {
				appendStr = "@"+appendStr;
			} 
		}
		editable.insert(index, appendStr);
		String editStr = editText.getText().toString();
		editText.setText(getSpannableStringBuilder(index, appendStr, editStr, data));
		mAtMap.add(data);
		editText.requestFocus();
		editText.setSelection(editStr.length());
	}
	
	private SpannableStringBuilder getSpannableStringBuilder(int index, String appendStr, String oldText, DoctorInfo nodeData) {
		if (oldText.length() > 0 && oldText.endsWith("@")) {
			oldText = oldText.substring(0, oldText.length() - 1);
		}
		SpannableStringBuilder spannableStringBuilder = getOldStringBuilder(oldText);
		spannableStringBuilder.setSpan(new AtTextAppearanceSpan(activity, nodeData), index, index + appendStr.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spannableStringBuilder;
	}
	
	private SpannableStringBuilder getOldStringBuilder(String oldText) {
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
		stringBuilder.append(oldText);
		if (mSpans != null) {
			Spanned s = editText.getEditableText();
			for (AtTextAppearanceSpan mspan : mSpans) {
				if (mspan instanceof AtTextAppearanceSpan) {
					int start = s.getSpanStart(mspan);
					int end = s.getSpanEnd(mspan);
					stringBuilder.setSpan(new AtTextAppearanceSpan(activity, mspan.getData()), start, end, SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}
		return stringBuilder;
	}
	
	public HashSet<DoctorInfo> getAtDoctorList() {
		return mAtMap;
	}
//    protected void toggleFaceImage(){
//        if(faceNormal.getVisibility() == View.VISIBLE){
//            showSelectedFaceImage();
//        }else{
//            showNormalFaceImage();
//        }
//    }
//    
//    private void showNormalFaceImage(){
//        faceNormal.setVisibility(View.VISIBLE);
//        faceChecked.setVisibility(View.INVISIBLE);
//    }
    
//    private void showSelectedFaceImage(){
//        faceNormal.setVisibility(View.INVISIBLE);
//        faceChecked.setVisibility(View.VISIBLE);
//    }
//    
//
//    @Override
//    public void onExtendMenuContainerHide() {
//        showNormalFaceImage();
//    }

}
