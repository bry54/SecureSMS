package com.securesms.contacts.model;

public class ReceiverUserModel {

    public ReceiverUserModel() {

    }

    public ReceiverUserModel(Integer int1, String string, String int2,
                             String string3) {
        this.id = int1;
        this.name = string;
        this.number = int2;
        this.password = string3;
    }

    private String name;
    private String number;
    private String password;
    private Integer id;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
