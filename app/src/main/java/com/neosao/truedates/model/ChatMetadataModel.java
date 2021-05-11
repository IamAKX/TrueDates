package com.neosao.truedates.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ChatMetadataModel implements Serializable {
    private String chatRoomId;
    private Date lastUpdateTimeStamp;
    UserBasicDetails participants;
    ArrayList<MessageModel> chats;

    public ChatMetadataModel(String chatRoomId, Date lastUpdateTimeStamp, UserBasicDetails participants, ArrayList<MessageModel> chats) {
        this.chatRoomId = chatRoomId;
        this.lastUpdateTimeStamp = lastUpdateTimeStamp;
        this.participants = participants;
        this.chats = chats;
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

    public UserBasicDetails getParticipants() {
        return participants;
    }

    public void setParticipants(UserBasicDetails participants) {
        this.participants = participants;
    }

    public ArrayList<MessageModel> getChats() {
        return chats;
    }

    public void setChats(ArrayList<MessageModel> chats) {
        this.chats = chats;
    }
}
