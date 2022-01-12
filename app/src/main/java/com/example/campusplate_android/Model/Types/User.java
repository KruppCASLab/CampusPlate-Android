package com.example.campusplate_android.Model.Types;

import com.example.campusplate_android.Credential;

public class User {
    String userName, GUID;
    int pin, userId, role, AccountValidated;
    UserCredential credential = new UserCredential(0,"Android");
    //TODO: use credential in here
    public User(String userName){ //post user
        this.userName = userName;
    }

    public User(String userName, int pin){ //post user
        this.userName = userName;
        this.pin = pin;
    }

    public String getUserName() {
        return userName;
    }

    public int getUserId(){
        return userId;
    }

    public int getPin() {
        return pin;
    }
}
