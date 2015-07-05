package com.securesms.items;

public class MessageItem {
	public int id;
	public int id_receivers;
	public String date;
	public String text;
	
	public MessageItem() {
		// TODO Auto-generated constructor stub
	}
	public MessageItem(int id,int id_receivers,String date,String text) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.id_receivers=id_receivers;
		this.date=date;
		this.text=text;
	}
}
