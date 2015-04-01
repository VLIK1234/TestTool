package amtt.epam.com.amtt;

import amtt.epam.com.amtt.storage.BaseStorage;
import android.app.Application;

/**
 * Created by Ivan_Bakach on 19.03.2015.
 */
public class CoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseStorage.initialize(this);
    }
}
