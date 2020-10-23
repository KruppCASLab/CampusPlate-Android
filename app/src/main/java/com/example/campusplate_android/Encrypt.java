package com.example.campusplate_android;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Encrypt {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";

    private byte[] encryption;
    private byte[] iv;
    private KeyStore keyStore;
    int KEY_SIZE = 256;

    public byte[] encryptText(final String alias, Credential credential)
            throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException,
            NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, IOException,
            InvalidAlgorithmParameterException, SignatureException, BadPaddingException,
            IllegalBlockSizeException, CertificateException {
        String cred = credential.getUserName() + "," + credential.getPassWord();
        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, createKey(alias));

        iv = cipher.getIV();

        return (encryption = cipher.doFinal(cred.getBytes(StandardCharsets.UTF_8)));
    }

    private SecretKey createKey(String alias) throws NoSuchProviderException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, KeyStoreException,
            CertificateException, IOException, UnrecoverableEntryException {
        keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
        keyStore.load(null);

        KeyGenerator keyGenerator = KeyGenerator
                .getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE);;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {


            if (!keyStore.containsAlias(alias)) {
                keyGenerator.init(new KeyGenParameterSpec.Builder(alias,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .build());
            } else {
                return ((KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null)).getSecretKey();
            }
        }
        else {
            keyGenerator = KeyGenerator.getInstance("AES", ANDROID_KEY_STORE);
            SecureRandom secureRandom = new SecureRandom(alias.getBytes());
            keyGenerator.init(KEY_SIZE, secureRandom);
        }
        return keyGenerator.generateKey();
    }

   public byte[] getEncryption() {
        return encryption;
    }

    public byte[] getIv() {
        return iv;
    }
}
