package com.example.campusplate_android;

import android.content.Context;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class CredentialManager {
    private final SharedPreferencesManager sharedPreferencesManager;
    private static final String KEYSTORE_NAME = "AndroidKeyStore";
    private KeyStore keyStore;

    public CredentialManager(SharedPreferencesManager sharedPreferencesManager) {
        this.sharedPreferencesManager = sharedPreferencesManager;
        try {
            keyStore = KeyStore.getInstance(KEYSTORE_NAME);
            keyStore.load(null);
        }
        catch(Exception e) {
            Log.e("Keystore", "Unable to initialize keystore");
        }
    }

    public String getUsername() {
        return sharedPreferencesManager.getString(SharedPreferencesManager.Key.USERNAME_STR);
    }

    public String getUserPassword() {
        String password = sharedPreferencesManager.getString(SharedPreferencesManager.Key.PASSWORD_STR);
        CipherEntry entry = CipherEntry.fromString(password);
        return decryptString(entry, getUsername());
    }

    public void storeUserCredentials(String user, String pass) {
        createNewKeys(user);
        CipherEntry entry = encryptString(pass, user);
        sharedPreferencesManager.put(SharedPreferencesManager.Key.USERNAME_STR, user);
        sharedPreferencesManager.put(SharedPreferencesManager.Key.PASSWORD_STR, entry.toString());
    }

    public void removeUserCredentials() {
        removeAllKeyStorePairs();
        sharedPreferencesManager.remove(SharedPreferencesManager.Key.USERKEY_STR,
                SharedPreferencesManager.Key.USERNAME_STR,
                SharedPreferencesManager.Key.PASSWORD_STR);

    }

    private void createNewKeys(String alias) {
        try {
            if (!keyStore.containsAlias(alias)) {
                KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder(alias, KeyProperties.PURPOSE_DECRYPT | KeyProperties.PURPOSE_ENCRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setRandomizedEncryptionRequired(true)
                        .build();
                KeyGenerator generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_NAME);
                generator.init(spec);

                generator.generateKey();
                storeUserAlias(alias);
            }
        } catch (Exception e) {
            Log.e("Keystore", "Unable to generate key");
        }
    }

    private CipherEntry encryptString(String clearText, String alias) {
        try {
            KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null);
            SecretKey key = entry.getSecretKey();

            Cipher inCipher = Cipher.getInstance("AES/GCM/NoPadding");
            inCipher.init(Cipher.ENCRYPT_MODE, key);

            byte [] iv = inCipher.getIV();

            byte [] encrypted = inCipher.doFinal(clearText.getBytes());
            String encryptedText =  Base64.encodeToString(encrypted, Base64.DEFAULT);

            return new CipherEntry(iv, encryptedText);
        } catch (Exception e) {
            return null;
        }
    }

    private String decryptString(CipherEntry cipherEntry, String alias) {
        try {
            KeyStore.SecretKeyEntry entry = (KeyStore.SecretKeyEntry) keyStore.getEntry(alias, null);
            SecretKey key = entry.getSecretKey();

            Cipher inCipher = Cipher.getInstance("AES/GCM/NoPadding");
            GCMParameterSpec spec = new GCMParameterSpec(128, cipherEntry.iv);
            inCipher.init(Cipher.DECRYPT_MODE, key, spec);

            byte [] decrypted = inCipher.doFinal(Base64.decode(cipherEntry.encryptedString.getBytes(), Base64.DEFAULT));
            String text = new String(decrypted);

            return text;

        } catch (Exception e) {
            return null;
        }
    }

    private void storeUserAlias(String alias) {
        sharedPreferencesManager.put(SharedPreferencesManager.Key.USERKEY_STR, alias);
    }

    private void removeKeyStorePairWithPublicKey(String key) {
        try {
            keyStore.deleteEntry(key);
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    private void removeAllKeyStorePairs() {
        try {
            keyStore = KeyStore.getInstance(KEYSTORE_NAME);
            keyStore.load(null);
            final Enumeration<String> entries = keyStore.aliases();

            while (entries.hasMoreElements()) {
                final String alias = entries.nextElement();
                keyStore.deleteEntry(alias);
            }
        } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    static public class CipherEntry {
        public byte [] iv;
        public String encryptedString;

        public CipherEntry(byte[] iv, String encryptedString) {
            this.iv = iv;
            this.encryptedString = encryptedString;
        }

        @NonNull
        @Override
        public String toString() {
            Gson gson = new Gson();
            return Base64.encodeToString(gson.toJson(this).getBytes(), Base64.DEFAULT);
        }

        static public CipherEntry fromString(String string) {
            Gson gson = new Gson();

            return gson.fromJson(new String(Base64.decode(string.getBytes(), Base64.DEFAULT)), CredentialManager.CipherEntry.class);
        }
    }
}
