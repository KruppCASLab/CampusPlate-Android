package com.example.campusplate_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.campusplate_android.ui.inputcode.InputCodeFragment;
import com.example.campusplate_android.ui.inputemail.InputEmailFragment;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

public class SignUpActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ServiceClient.getInstance(this.getApplicationContext());
        setContentView(R.layout.activity_sign_up);

    }
}
