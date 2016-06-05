package com.securesms.newMessage.model;

/**
 * Created by admin on 2016-06-03.
 */
public class NewMessageModel {
    private String phoneNo;
    private String message;

    public NewMessageModel(String phoneNo, String message) {
        this.phoneNo = phoneNo;
        this.message = message;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
