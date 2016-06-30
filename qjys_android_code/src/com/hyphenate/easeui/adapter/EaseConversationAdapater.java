package com.hyphenate.easeui.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.DateUtils;
import com.rmtech.qjys.R;
import com.rmtech.qjys.model.CaseInfo;
import com.rmtech.qjys.model.DoctorInfo;
import com.rmtech.qjys.model.UserContext;
import com.rmtech.qjys.utils.DoctorListManager;
import com.rmtech.qjys.utils.DoctorListManager.OnGetDoctorInfoCallback;
import com.rmtech.qjys.utils.GroupAndCaseListManager;

/**
 * 会话列表adapter
 *
 */
public class EaseConversationAdapater extends ArrayAdapter<EMConversation> {
    private static final String TAG = "ChatAllHistoryAdapter";
    private List<EMConversation> conversationList;
    private List<EMConversation> copyConversationList;
    private ConversationFilter conversationFilter;
    private boolean notiyfyByFilter;
    
//    protected int primaryColor;
//    protected int secondaryColor;
//    protected int timeColor;
//    protected int primarySize;
//    protected int secondarySize;
//    protected float timeSize;

    public EaseConversationAdapater(Context context, int resource,
            List<EMConversation> objects) {
        super(context, resource, objects);
        conversationList = objects;
        copyConversationList = new ArrayList<EMConversation>();
        copyConversationList.addAll(objects);
    }

    @Override
    public int getCount() {
        return conversationList.size();
    }

    @Override
    public EMConversation getItem(int arg0) {
        if (arg0 < conversationList.size()) {
            return conversationList.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private SpannableStringBuilder createAtMeStr(Spannable message) {
    	String attext = "[有人@我]";
        SpannableStringBuilder style=new SpannableStringBuilder(attext); 
        style.setSpan(new ForegroundColorSpan(Color.RED),0,attext.length(),Spannable.SPAN_EXCLUSIVE_INCLUSIVE); 
        style.append(message);
        return style;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ease_row_chat_history, parent, false);
        }
       ViewHolder holder2 = (ViewHolder) convertView.getTag();
        if (holder2 == null) {
        	holder2 = new ViewHolder();
        	holder2.name = (TextView) convertView.findViewById(R.id.name);
        	holder2.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);
        	holder2.message = (TextView) convertView.findViewById(R.id.message);
        	holder2.time = (TextView) convertView.findViewById(R.id.time);
        	holder2.avatar = (ImageView) convertView.findViewById(R.id.avatar);
        	holder2.msgState = convertView.findViewById(R.id.msg_state);
//            holder.list_itease_layout = (RelativeLayout) convertView.findViewById(R.id.list_itease_layout);
            convertView.setTag(holder2);
        }
        final ViewHolder holder = (ViewHolder) convertView.getTag();
//        holder.list_itease_layout.setBackgroundResource(R.drawable.ease_mm_listitem);
//
        // 获取与此用户/群组的会话
        EMConversation conversation = getItem(position);
        // 获取用户username或者群组groupid
        String username = conversation.getUserName();
//        String nick = DoctorListManager.getInstance().getNickByHXid(username);
//        if(nick != null) {
//        	username = nick;
//        }
        if (conversation.getType() == EMConversationType.GroupChat) {
            // 群聊消息，显示群聊头像
            holder.avatar.setImageResource(R.drawable.ic_group);
            EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
            CaseInfo info = GroupAndCaseListManager.getInstance().getCaseInfoByGroupId(username);
            if(info != null) {
            	holder.name.setText(info.getShowName());
            } else {
            	holder.name.setText(group != null ? group.getGroupName() : username);
            }
        } else if(conversation.getType() == EMConversationType.ChatRoom){
            holder.avatar.setImageResource(R.drawable.ease_group_icon);
            EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(username);
            holder.name.setText(room != null && !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
        }else {
            EaseUserUtils.setUserAvatar(getContext(), username, holder.avatar);
            EaseUserUtils.setUserNick(username, holder.name);
        }

        if (conversation.getUnreadMsgCount() > 0) {
            // 显示与此用户的消息未读数
            holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
            holder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
        }
        boolean someoneAtme  = false;
        if (conversation.getAllMsgCount() != 0) {
            // 把最后一条消息的内容作为item的message内容
   		 	EMMessage lastMessage = conversation.getLastMessage();
            final String lastStr =  EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext()));

        	if(conversation.getType() == EMConversationType.GroupChat) {
                 String atStr = null;
                 try {
     				atStr = lastMessage.getStringAttribute("at");
     			} catch (HyphenateException e) {
     				// TODO Auto-generated catch block
     				//e.printStackTrace();
     			}
                 holder.message.setText("");
                 String[] atArray = null;
                 if(!TextUtils.isEmpty(atStr)) {
                 	atArray = atStr.split(",");
                 	if(atArray != null) {
     	            	for(String str : atArray) {
     	            		if(UserContext.getInstance().isMyself(str)) {
     	            			someoneAtme = true;
     	            			break;
     	            		}
     	            	}
                 	}
                 }
        	}
           
            
            if(UserContext.getInstance().isMyself(lastMessage.getFrom())) {
            	
            	Spannable ss = EaseSmileUtils.getSmiledText(getContext(),"我: " + lastStr);
            	if(someoneAtme) {
            		ss = createAtMeStr(ss);
            	}
                
            	 holder.message.setText(ss,BufferType.SPANNABLE);
            } else {
            	final boolean fsomeoneAtme = someoneAtme;
            	if(conversation.getType() == EMConversationType.GroupChat && TextUtils.equals(username, lastMessage.getFrom())) {
					holder.message.setText(EaseSmileUtils.getSmiledText(getContext(), lastStr), BufferType.SPANNABLE);
            	} else {
            	DoctorListManager.getInstance().getDoctorInfoByHXid(lastMessage.getFrom(), new OnGetDoctorInfoCallback() {
					
					@Override
					public void onGet(DoctorInfo info) {
						if(info != null) {
							Spannable ss = EaseSmileUtils.getSmiledText(getContext(), info.getDisplayName() +": " +lastStr);
							if(fsomeoneAtme) {
			            		ss = createAtMeStr(ss);
			            	}
							holder.message.setText(ss, BufferType.SPANNABLE);
						} else {
							Spannable ss = EaseSmileUtils.getSmiledText(getContext(), lastStr);
							if(fsomeoneAtme) {
			            		ss = createAtMeStr(ss);
			            	}
							holder.message.setText(ss, BufferType.SPANNABLE);
						}
					}
				});
            	}
            };
           

            holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }
        }
        
        //设置自定义属性
//        holder.name.setTextColor(primaryColor);
//        holder.message.setTextColor(secondaryColor);
//        holder.time.setTextColor(timeColor);
//        if(primarySize != 0)
//            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
//        if(secondarySize != 0)
//            holder.message.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondarySize);
//        if(timeSize != 0)
//            holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeSize);

        return convertView;
    }
    
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if(!notiyfyByFilter){
            copyConversationList.clear();
            copyConversationList.addAll(conversationList);
            notiyfyByFilter = false;
        }
    }
    
    @Override
    public Filter getFilter() {
        if (conversationFilter == null) {
            conversationFilter = new ConversationFilter(conversationList);
        }
        return conversationFilter;
    }
    

//    public void setPrimaryColor(int primaryColor) {
//        this.primaryColor = primaryColor;
//    }
//
//    public void setSecondaryColor(int secondaryColor) {
//        this.secondaryColor = secondaryColor;
//    }
//
//    public void setTimeColor(int timeColor) {
//        this.timeColor = timeColor;
//    }
//
//    public void setPrimarySize(int primarySize) {
//        this.primarySize = primarySize;
//    }
//
//    public void setSecondarySize(int secondarySize) {
//        this.secondarySize = secondarySize;
//    }
//
//    public void setTimeSize(float timeSize) {
//        this.timeSize = timeSize;
//    }





    private class ConversationFilter extends Filter {
        List<EMConversation> mOriginalValues = null;

        public ConversationFilter(List<EMConversation> mList) {
            mOriginalValues = mList;
        }

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<EMConversation>();
            }
            if (prefix == null || prefix.length() == 0) {
                results.values = copyConversationList;
                results.count = copyConversationList.size();
            } else {
                String prefixString = prefix.toString();
                final int count = mOriginalValues.size();
                final ArrayList<EMConversation> newValues = new ArrayList<EMConversation>();

                for (int i = 0; i < count; i++) {
                    final EMConversation value = mOriginalValues.get(i);
                    String username = value.getUserName();
                    
                    EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
                    if(group != null){
                        username = group.getGroupName();
                    }else{
                        EaseUser user = EaseUserUtils.getUserInfo(username);
                        // TODO: not support Nick anymore
//                        if(user != null && user.getNick() != null)
//                            username = user.getNick();
                    }

                    // First match against the whole ,non-splitted value
                    if (username.startsWith(prefixString)) {
                        newValues.add(value);
                    } else{
                          final String[] words = username.split(" ");
                            final int wordCount = words.length;

                            // Start at index 0, in case valueText starts with space(s)
                            for (int k = 0; k < wordCount; k++) {
                                if (words[k].startsWith(prefixString)) {
                                    newValues.add(value);
                                    break;
                                }
                            }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            conversationList.clear();
            conversationList.addAll((List<EMConversation>) results.values);
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }

        }

    }
    
    
    private static class ViewHolder {
        /** 和谁的聊天记录 */
        TextView name;
        /** 消息未读数 */
        TextView unreadLabel;
        /** 最后一条消息的内容 */
        TextView message;
        /** 最后一条消息的时间 */
        TextView time;
        /** 用户头像 */
        ImageView avatar;
        /** 最后一条消息的发送状态 */
        View msgState;
        /** 整个list中每一行总布局 */
//        RelativeLayout list_itease_layout;

    }
}

