package com.example.campusplate_android.Model.Types;

public class User {
    String userName, GUID;
    int pin, userId, role, AccountValidated;
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
