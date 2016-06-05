package com.securesms.contacts.model;

/**
 * Created by admin on 2016-06-05.
 */
public class ContactModel {
    private String name;
    private String number;

    public ContactModel() {
    }

    public ContactModel(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
