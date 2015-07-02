package com.example.ivan_bakach.testpermissionclient;

import android.app.Application;

/**
 * Created by Ivan_Bakach on 02.06.2015.
 */
public class CoreApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
//        InjectionManger.INSTANCE.injection(CoreApplication.this);
    }
}
