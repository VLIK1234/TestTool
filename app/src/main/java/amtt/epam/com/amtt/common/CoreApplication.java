package amtt.epam.com.amtt.common;

import android.app.Application;
import android.content.Context;

/**
 @author Artsiom_Kaliaha
 @version on 18.06.2015
 */

public abstract class CoreApplication extends Application {

    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        performRegistration();
    }

    public static Context getContext() {
        return sContext;
    }

    public abstract void performRegistration();

}
