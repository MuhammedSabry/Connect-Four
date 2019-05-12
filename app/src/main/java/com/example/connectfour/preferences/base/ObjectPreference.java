package com.example.connectfour.preferences.base;

import android.content.SharedPreferences;

import com.google.gson.Gson;

public class ObjectPreference<T> extends BasePreference {
    private final T defaultValue;
    private final Class<T> classOfObject;

    public ObjectPreference(SharedPreferences preferences, String key, T defaultValue, Class<T> classOfObject) {
        super(preferences, key);
        this.defaultValue = defaultValue;
        this.classOfObject = classOfObject;
    }

    public T get() {
        String json = preferences.getString(key, null);
        if (json == null) {
            return defaultValue;
        }

        return new Gson().fromJson(json, classOfObject);
    }

    public void set(T value) {
        String json = new Gson().toJson(value);
        preferences.edit().putString(key, json).apply();
    }
}

