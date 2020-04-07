package com.example.pluginproject;

import android.app.Application;
import android.content.Context;

import fu.wanke.skin.SkinManager;

public class App extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SkinManager.getInstance().init(this);
    }
}
