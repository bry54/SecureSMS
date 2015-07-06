package com.securesms.items;

public class MessageItem {
	public long id;
	public long id_receivers;
	public String date;
	public String text;
	public int rec;
	public int read;

	public MessageItem() {
		// TODO Auto-generated constructor stub
	}
	public MessageItem(long id,long id_receivers,String date,String text,int rec,int read) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.id_receivers=id_receivers;
		this.date=date;
		this.text=text;
		this.rec=rec;
		this.read=read;
	}
}
