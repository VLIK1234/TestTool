package amtt.epam.com.amtt;

import amtt.epam.com.amtt.util.PreferenceUtils;
import android.app.Application;

/**
 * Created by Ivan_Bakach on 19.03.2015.
 */
public class CoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferenceUtils.initDefaultPreference(this);
    }
}
