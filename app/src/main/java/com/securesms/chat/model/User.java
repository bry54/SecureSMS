package com.securesms.chat.model;

import java.io.Serializable;

/**
 * Created by admin on 2016-06-01.
 */
public class User implements Serializable{
    private int recId;
    private String nick;
    private String number;

    public User(int recId, String nick, String number) {
        this.recId = recId;
        this.nick = nick;
        this.number = number;
    }

    public int getRecId() {
        return recId;
    }

    public void setRecId(int recId) {
        this.recId = recId;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
