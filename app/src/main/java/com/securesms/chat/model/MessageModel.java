package com.securesms.chat.model;

public class MessageModel {
    public long id;
    public long id_receivers;
    public String date;
    public String text;
    public int rec;
    public int read;

    public MessageModel() {
    }

    public MessageModel(long id, long id_receivers, String date, String text, int rec, int read) {
        this.id = id;
        this.id_receivers = id_receivers;
        this.date = date;
        this.text = text;
        this.rec = rec;
        this.read = read;
    }

    public MessageModel(String text, String date, int rec) {
        this.text = text;
        this.date = date;
        this.rec = rec;
    }
}
