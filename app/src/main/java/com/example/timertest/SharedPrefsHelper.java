package com.example.timertest;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


import java.util.Map;

/**
 * Created by ddalal on 2/6/18.
 */

public final class SharedPrefsHelper {

    private static final String DEFAULT_STRING_VALUE = null;

    private static SharedPrefsHelper instance;
    private final SharedPreferences defaultSharedPreferences;
    private final SharedPreferences.Editor editor;

    private SharedPrefsHelper(Context context) {
        defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        editor = defaultSharedPreferences.edit();
    }

    public synchronized static SharedPrefsHelper getInstance(Context context) {
        if (instance == null) {
            instance = new SharedPrefsHelper(context);
        }
        return instance;
    }

    public void putString(final String key, final String value) {
        editor.putString(key, value);
        editor.commit();
    }

    public void putStrings(final Map<String, String> keys) {
        if (keys.isEmpty() == false) {
            for (Map.Entry<String, String> entry : keys.entrySet()) {
                editor.putString(entry.getKey(), entry.getValue());
            }

            editor.commit();
        }
    }

    public String getString(final String key) {
        return defaultSharedPreferences.getString(key, DEFAULT_STRING_VALUE);
    }

    public void removeString(final String... keys) {
        if (keys.length > 0) {
            for (String key : keys) {
                if (defaultSharedPreferences.contains(key)) {
                    editor.remove(key);
                }
            }

            editor.commit();
        }
    }

    public boolean containsString(final String key) {
        return defaultSharedPreferences.contains(key);
    }

    public void putBoolean(final String key, final boolean value) {
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defaultValue){
        boolean result = defaultSharedPreferences.getBoolean(key, defaultValue);
        return result;
    }

    public void putLong(final String key, final long value) {
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(final String key, final long defaultValue){
        final long result = defaultSharedPreferences.getLong(key, defaultValue);
        return result;
    }

    public void putInt(final String key, final int value) {
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(final String key, final int defaultValue){
        final int result = defaultSharedPreferences.getInt(key, defaultValue);
        return result;
    }


    public void remove(final String key) {
        editor.remove(key);
        editor.commit();
    }
}
