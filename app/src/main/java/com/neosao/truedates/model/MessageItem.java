package com.neosao.truedates.model;

import java.util.ArrayList;
import java.util.Date;

public class MessageItem {

    private String chatRoomId;
    private Date lastUpdateTimeStamp;
    private String lastMessage;
    private String lastMessageSentBy;
    private ArrayList<UserBasicDetails> participants;

    public MessageItem() {
    }

    public MessageItem(String chatRoomId, Date lastUpdateTimeStamp, String lastMessage, String lastMessageSentBy, ArrayList<UserBasicDetails> participants) {
        this.chatRoomId = chatRoomId;
        this.lastUpdateTimeStamp = lastUpdateTimeStamp;
        this.lastMessage = lastMessage;
        this.lastMessageSentBy = lastMessageSentBy;
        this.participants = participants;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public Date getLastUpdateTimeStamp() {
        return lastUpdateTimeStamp;
    }

    public void setLastUpdateTimeStamp(Date lastUpdateTimeStamp) {
        this.lastUpdateTimeStamp = lastUpdateTimeStamp;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageSentBy() {
        return lastMessageSentBy;
    }

    public void setLastMessageSentBy(String lastMessageSentBy) {
        this.lastMessageSentBy = lastMessageSentBy;
    }

    public ArrayList<UserBasicDetails> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<UserBasicDetails> participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "MessageItem{" +
                "chatRoomId='" + chatRoomId + '\'' +
                ", lastUpdateTimeStamp=" + lastUpdateTimeStamp +
                ", lastMessage='" + lastMessage + '\'' +
                ", lastMessageSentBy='" + lastMessageSentBy + '\'' +
                ", participants=" + participants +
                '}';
    }
}

