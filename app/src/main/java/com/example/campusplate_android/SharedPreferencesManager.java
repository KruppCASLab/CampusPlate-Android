package com.example.campusplate_android;

import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private final SharedPreferences mPrefs;

    public enum Key {
        USERNAME_STR,
        PASSWORD_STR,
        USERKEY_STR,
    }

    public SharedPreferencesManager(SharedPreferences sharedPreferences) {
        mPrefs = sharedPreferences;
    }

    public void put(Key key, String val) {
        mPrefs.edit().putString(key.name(), val).apply();
    }

    public String getString(Key key) {
        return mPrefs.getString(key.name(), null);
    }

    /**
     * Deletes keys passed as args.
     *
     * @param keys list of keys to delete
     */
    public void remove(Key... keys) {
        for (Key key : keys) {
            mPrefs.edit().remove(key.name()).apply();
        }
    }
}
