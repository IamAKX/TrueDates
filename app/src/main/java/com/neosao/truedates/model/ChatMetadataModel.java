package com.neosao.truedates.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class ChatMetadataModel implements Serializable {
    private String chatRoomId;
    private Date lastUpdateTimeStamp;
    UserBasicDetails participants;

    public ChatMetadataModel(String chatRoomId, Date lastUpdateTimeStamp, UserBasicDetails participants) {
        this.chatRoomId = chatRoomId;
        this.lastUpdateTimeStamp = lastUpdateTimeStamp;
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

    public UserBasicDetails getParticipants() {
        return participants;
    }

    public void setParticipants(UserBasicDetails participants) {
        this.participants = participants;
    }

    @Override
    public String toString() {
        return "ChatMetadataModel{" +
                "chatRoomId='" + chatRoomId + '\'' +
                ", lastUpdateTimeStamp=" + lastUpdateTimeStamp +
                ", participants=" + participants +
                '}';
    }
}
