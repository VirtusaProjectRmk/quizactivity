package com.example.harisha.virtusaquiz;

import android.app.Application;

import com.firebase.client.Firebase;

public class Quiz extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Firebase.setAndroidContext(this);

    }
}
