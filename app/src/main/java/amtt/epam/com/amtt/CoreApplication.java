package amtt.epam.com.amtt;

import android.app.Application;
import android.database.Cursor;

import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.service.TopButtonService;
import amtt.epam.com.amtt.util.ActiveUser;
import amtt.epam.com.amtt.util.Constants;
import amtt.epam.com.amtt.util.ContextHolder;
import amtt.epam.com.amtt.util.IOUtils;

/**
 * Created by Ivan_Bakach on 19.03.2015.
 */
public class CoreApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextHolder.setContext(getApplicationContext());
        TopButtonService.start(this);
    }

}
