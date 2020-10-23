package com.example.campusplate_android;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

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
