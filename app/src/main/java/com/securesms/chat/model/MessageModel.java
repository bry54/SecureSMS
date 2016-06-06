package com.securesms.chat.model;

public class MessageModel {
    private long id;
    private long id_receivers;
    private String date;
    private String text;
    private int rec;
    private int read;

    public MessageModel() {
    }

    public MessageModel(String text, String date, int rec) {
        this.text = text;
        this.date = date;
        this.rec = rec;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId_receivers() {
        return id_receivers;
    }

    public void setId_receivers(long id_receivers) {
        this.id_receivers = id_receivers;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getRec() {
        return rec;
    }

    public void setRec(int rec) {
        this.rec = rec;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }
}
