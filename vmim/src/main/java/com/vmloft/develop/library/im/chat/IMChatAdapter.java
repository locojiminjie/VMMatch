package com.vmloft.develop.library.im.chat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.vmloft.develop.library.im.chat.msgitem.IMMsgItem;
import com.vmloft.develop.library.im.chat.msgitem.IMTextMsgItem;
import com.vmloft.develop.library.im.chat.msgitem.IMUnknownMsgItem;
import com.vmloft.develop.library.im.common.IMConstants;
import com.vmloft.develop.library.im.conversation.IMConversationManager;
import com.vmloft.develop.library.tools.adapter.VMAdapter;
import com.vmloft.develop.library.tools.adapter.VMHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by lzan13 on 2019/5/23 19:58
 *
 * IM 聊天界面适配器
 */
public class IMChatAdapter extends VMAdapter<EMMessage, IMChatAdapter.ChatHolder> {

    private String mId;
    private int mChatType;

    public IMChatAdapter(Context context, String id, int chatType) {
        super(context);
        mId = id;
        mChatType = chatType;
        mDataList = IMConversationManager.getInstance().getCacheMessages(mId, mChatType);
    }

    @NonNull
    @Override
    public ChatHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int type) {
        return new ChatHolder(createMsgItem(type));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        ((IMMsgItem) holder.itemView).onBind(getItemData(position));
    }

    @Override
    public int getItemViewType(int position) {
        EMMessage message = getItemData(position);
        int itemType;
        if (message.getType() == EMMessage.Type.TXT) {
            // 文本类型
            itemType = IMConstants.IM_CHAT_TYPE_TEXT;
        } else if (message.getType() == EMMessage.Type.IMAGE) {
            itemType = message.direct() == EMMessage.Direct.RECEIVE ? IMConstants.IM_CHAT_TYPE_IMAGE_RECEIVE : IMConstants.IM_CHAT_TYPE_IMAGE_SEND;
        } else if (message.getType() == EMMessage.Type.VIDEO) {
            itemType = message.direct() == EMMessage.Direct.RECEIVE ? IMConstants.IM_CHAT_TYPE_VIDEO_RECEIVE : IMConstants.IM_CHAT_TYPE_TEXT_SEND;
        } else if (message.getType() == EMMessage.Type.LOCATION) {
            itemType = message.direct() == EMMessage.Direct.RECEIVE ? IMConstants.IM_CHAT_TYPE_LOCATION_RECEIVE : IMConstants.IM_CHAT_TYPE_LOCATION_SEND;
        } else if (message.getType() == EMMessage.Type.VOICE) {
            itemType = message.direct() == EMMessage.Direct.RECEIVE ? IMConstants.IM_CHAT_TYPE_VOICE_RECEIVE : IMConstants.IM_CHAT_TYPE_VOICE_SEND;
        } else if (message.getType() == EMMessage.Type.FILE) {
            itemType = message.direct() == EMMessage.Direct.RECEIVE ? IMConstants.IM_CHAT_TYPE_FILE_RECEIVE : IMConstants.IM_CHAT_TYPE_FILE_SEND;
        } else {
            // 未知类型，显示提示文本
            itemType = IMConstants.IM_CHAT_TYPE_UNKNOWN;
        }
        // 读取扩展消息类型，如果没有扩展那就是默认文本消息
        itemType = message.getIntAttribute(IMConstants.IM_CHAT_MSG_TYPE, itemType);
        return itemType;
    }

    /**
     * 创建一个消息 Item
     */
    private IMMsgItem createMsgItem(int type) {
        IMMsgItem itemView = null;
        switch (type) {
            case IMConstants.IM_CHAT_TYPE_SYSTEM:
            case IMConstants.IM_CHAT_TYPE_RECALL:
                break;
            case IMConstants.IM_CHAT_TYPE_TEXT:
                itemView = new IMTextMsgItem(mContext, this, type);
                break;
            case IMConstants.IM_CHAT_TYPE_IMAGE_RECEIVE:
            case IMConstants.IM_CHAT_TYPE_IMAGE_SEND:
            case IMConstants.IM_CHAT_TYPE_VIDEO_RECEIVE:
            case IMConstants.IM_CHAT_TYPE_VIDEO_SEND:
            case IMConstants.IM_CHAT_TYPE_LOCATION_RECEIVE:
            case IMConstants.IM_CHAT_TYPE_LOCATION_SEND:
            case IMConstants.IM_CHAT_TYPE_VOICE_RECEIVE:
            case IMConstants.IM_CHAT_TYPE_VOICE_SEND:
            case IMConstants.IM_CHAT_TYPE_FILE_RECEIVE:
            case IMConstants.IM_CHAT_TYPE_FILE_SEND:
            case IMConstants.IM_CHAT_TYPE_CALL_RECEIVE:
            case IMConstants.IM_CHAT_TYPE_CALL_SEND:
            case IMConstants.IM_CHAT_TYPE_UNKNOWN: // 未知
            default:
                itemView = new IMUnknownMsgItem(mContext, this, IMConstants.IM_CHAT_TYPE_UNKNOWN);
                break;
        }
        return itemView;
    }

    /**
     * 加载消息列表
     */
    private void updateData() {
        mDataList = IMConversationManager.getInstance().getCacheMessages(mId, mChatType);
    }

    /**
     * 更新插入
     *
     * @param position 插入位置
     */
    public void updateInsert(int position) {
        updateInsert(position, 1);
    }

    /**
     * 更新插入
     *
     * @param position 插入位置
     * @param count    插入个数
     */
    public void updateInsert(int position, int count) {
        updateData();
        notifyItemRangeInserted(position, count);
    }

    /**
     * 更新移除
     *
     * @param position 删除位置
     */
    public void updateRemove(int position) {
        updateRemove(position, 1);
    }

    /**
     * 更新移除
     *
     * @param position 删除位置
     * @param count    删除个数
     */
    public void updateRemove(int position, int count) {
        updateData();
        notifyItemRangeRemoved(position, count);
    }

    /**
     * 更新改变
     *
     * @param position 改变位置
     */
    public void updateChange(int position) {
        updateChange(position, 1);
    }

    /**
     * 更新改变
     *
     * @param position 改变位置
     * @param count    改变个数
     */
    public void updateChange(int position, int count) {
        updateData();
        notifyItemRangeChanged(position, count, 1);
    }

    /**
     * IM 消息 ItemHolder
     */
    static class ChatHolder extends VMHolder {

        public ChatHolder(View itemView) {
            super(itemView);
        }
    }
}
