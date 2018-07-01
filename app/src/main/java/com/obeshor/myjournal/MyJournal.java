package com.obeshor.myjournal;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import com.firebase.client.Firebase;
import com.google.firebase.database.FirebaseDatabase;

public class MyJournal extends Application {

    // Intent Extra + Bundle Argument + Saved Instance State
    public static final String ENTRY_ID = "entry_id";
    public static final String ENTRY_OBJECT = "entry_object";
    public static final String ENTRY_LIST = "entry_list";

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);
/*
        if(!FirebaseApp.getApps(this).isEmpty()){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
*/
        Picasso.Builder builder = new Picasso.Builder(this) ;
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE)) ;
        Picasso built = builder.build() ;
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);

    }
}
