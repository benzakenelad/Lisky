package com.example.elad.liskyproject;

import android.app.Application;
import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
        Picasso built = builder.build();

        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
//        built.setIndicatorsEnabled(false);
        Picasso.setSingletonInstance(built);
    }

    public static Context getAppContext(){
        return context;
    }
}
