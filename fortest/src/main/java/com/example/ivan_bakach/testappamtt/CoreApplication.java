package com.example.ivan_bakach.testappamtt;

import android.app.Application;

/**
 @author Ivan_Bakach
 @version on 02.06.2015
 */

public class CoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        InjectionManger.INSTANCE.injection(CoreApplication.this);
    }
}
