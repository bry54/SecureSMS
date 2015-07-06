package com.securesms.items;

public class ReceiverItem {

    public ReceiverItem() {

    }

    public ReceiverItem(long int1, String string, String int2,
                        String string3) {
        this.id = int1;
        this.name = string;
        this.number = int2;
        this.code = string3;
    }

    public String name;
    public String number;
    public String code;
    public long id;

}
