package com.securesms.items;

public class ReceiverItem {

	public ReceiverItem() {

	}

	public ReceiverItem(int int1, String string, String string2, int int2,
			String string3) {
		this.id = int1;
		this.name = string;
		this.surname = string2;
		this.number = int2;
		this.code = string3;
	}

	public String name;
	public String surname;
	public int number;
	public String code;
	public int id;

}
