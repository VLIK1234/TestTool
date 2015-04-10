package amtt.epam.com.amtt;

import android.app.Application;

import amtt.epam.com.amtt.service.TopButtonService;
import amtt.epam.com.amtt.storage.BaseStorage;

/**
 * Created by Ivan_Bakach on 19.03.2015.
 */
public class CoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BaseStorage.initialize(this);
        TopButtonService.start(this);
    }
}
