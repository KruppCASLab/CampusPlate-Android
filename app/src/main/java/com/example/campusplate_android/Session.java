package com.example.campusplate_android;

public class Session {
    private static Session session;
    private Credential credential;


    private Session() {

    }

    synchronized public static Session getInstance(){
        if(session == null){
            session = new Session();
        }
        return session;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }
}
