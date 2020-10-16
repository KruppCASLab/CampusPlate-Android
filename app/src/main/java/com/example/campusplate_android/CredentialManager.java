package com.example.campusplate_android;

import android.content.Context;

public class CredentialManager {
    private static CredentialManager credentialManager;
    private Credential credential;
    private Context ctx;

    synchronized public static CredentialManager shared(){
        if(credentialManager == null){
            credentialManager = new CredentialManager();
        }
        return credentialManager;
    }

    public CredentialManager saveCredential(Credential credential) {
        this.credential = credential;
        return null;
    }



    public Credential getCredential(){
        return credential;
    }
}
