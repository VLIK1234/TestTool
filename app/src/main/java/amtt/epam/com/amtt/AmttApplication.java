package amtt.epam.com.amtt;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import amtt.epam.com.amtt.util.ThreadManager;
import io.fabric.sdk.android.Fabric;

/**
 @author Ivan_Bakach
 @version on 19.03.2015
 */

public class AmttApplication extends Application {

    private static Context sContext;

    public static Context getContext() {
        return sContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        performRegistration();

        Fabric.with(new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)
                .build());

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);
    }

    public void performRegistration() {
        ThreadManager.performRegistration();
    }

}
