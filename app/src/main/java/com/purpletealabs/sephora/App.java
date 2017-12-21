package com.purpletealabs.sephora;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Initialize fresco image loading library
        Fresco.initialize(this);
    }
}
