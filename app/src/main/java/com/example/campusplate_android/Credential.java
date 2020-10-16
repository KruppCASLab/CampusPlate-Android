package com.example.campusplate_android;

public class Credential {
    String userName, passWord;

    public Credential(String userName) {
        this.userName = userName;
    }
    public Credential(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }
}
