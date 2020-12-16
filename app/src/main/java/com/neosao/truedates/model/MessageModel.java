package com.neosao.truedates.model;

import java.io.Serializable;
import java.util.Date;

public class MessageModel implements Serializable {
    private String message;
    private String senderID;
    private String senderName;
    private String senderProfileImage;
    private Date messageTimestamp;
    private String messageID;

    public MessageModel() {
    }

    public MessageModel(String message, String senderID, String senderName, String senderProfileImage, Date messageTimestamp, String messageID) {
        this.message = message;
        this.senderID = senderID;
        this.senderName = senderName;
        this.senderProfileImage = senderProfileImage;
        this.messageTimestamp = messageTimestamp;
        this.messageID = messageID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderProfileImage() {
        return senderProfileImage;
    }

    public void setSenderProfileImage(String senderProfileImage) {
        this.senderProfileImage = senderProfileImage;
    }

    public Date getMessageTimestamp() {
        return messageTimestamp;
    }

    public void setMessageTimestamp(Date messageTimestamp) {
        this.messageTimestamp = messageTimestamp;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "message='" + message + '\'' +
                ", senderID='" + senderID + '\'' +
                ", senderName='" + senderName + '\'' +
                ", senderProfileImage='" + senderProfileImage + '\'' +
                ", messageTimestamp=" + messageTimestamp +
                ", messageID='" + messageID + '\'' +
                '}';
    }
}
