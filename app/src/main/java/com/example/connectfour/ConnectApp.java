package com.example.connectfour;

import android.app.Application;
import android.content.Context;

public class ConnectApp extends Application {

    private static Context applicationContext;

    public static Context getContext() {
        return applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
    }
}
