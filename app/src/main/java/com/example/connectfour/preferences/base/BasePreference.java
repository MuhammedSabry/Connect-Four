package com.example.connectfour.preferences.base;

import android.content.SharedPreferences;

abstract class BasePreference {

    final SharedPreferences preferences;
    final String key;

    BasePreference(SharedPreferences preferences, String key) {
        this.preferences = preferences;
        this.key = key;
    }

    public boolean isSet() {
        return preferences.contains(key);
    }

    public void delete() {
        preferences.edit().remove(key).apply();
    }

}
