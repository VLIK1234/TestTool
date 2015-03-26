package amtt.epam.com.amtt;

import android.app.Application;

import amtt.epam.com.amtt.storage.ScreenNumber;

/**
 * Created by Ivan_Bakach on 19.03.2015.
 */
public class CoreApplication extends Application {

    public static int SCREEN_NUMBER;

    @Override
    public void onCreate() {
        super.onCreate();
        ScreenNumber.initialize(this);
        SCREEN_NUMBER = ScreenNumber.getNumber();
    }
}
