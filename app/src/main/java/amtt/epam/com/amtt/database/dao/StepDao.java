package amtt.epam.com.amtt.database.dao;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;

import java.io.File;
import java.io.IOException;

import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.table.ActivityInfoTable;
import amtt.epam.com.amtt.util.ContentValuesUtil;

/**
 * Created by Artsiom_Kaliaha on 12.05.2015.
 */
public class StepDao extends AbstractDao<Step> {

    public static final String TAG = StepDao.class.getSimpleName();

    @Override
    public int add(Step object) throws IOException, NameNotFoundException {
        String screenPath = object.saveScreen();

        ComponentName activityComponent = object.getActivityComponent();
        int existingActivityInfo = sContext.getContentResolver().query(
                AmttUri.ACTIVITY_META.get(),
                new String[]{ActivityInfoTable._ACTIVITY_NAME},
                ActivityInfoTable._ACTIVITY_NAME + "=?",
                new String[]{activityComponent.getClassName()},
                null).getCount();

        //if there is no records about current app in db
        if (existingActivityInfo == 0) {
            ActivityInfo activityInfo = sContext
                    .getPackageManager()
                    .getActivityInfo(activityComponent, PackageManager.GET_META_DATA & PackageManager.GET_INTENT_FILTERS);
            ContentValues activityContentValues = ContentValuesUtil.getValuesForActivityInfo(activityInfo);
            sContext.getContentResolver().insert(AmttUri.ACTIVITY_META.get(), activityContentValues);
        }

        ContentValues stepContentValues = ContentValuesUtil.getValuesForStep(object.getStepNumber(), activityComponent, screenPath);
        Uri insertedStepUri = sContext.getContentResolver().insert(AmttUri.STEP.get(), stepContentValues);
        return Integer.valueOf(insertedStepUri.getLastPathSegment());
    }

    public void removeAll() {
        sContext.getContentResolver().delete(AmttUri.ACTIVITY_META.get(), null, null);
        sContext.getContentResolver().delete(AmttUri.STEP.get(), null, null);

        File screenshotDirectory = new File(Step.getScreenBasePath());
        if (!screenshotDirectory.exists()) {
            screenshotDirectory.mkdir();
        } else {
            for (File screenshot : screenshotDirectory.listFiles()) {
                screenshot.delete();
            }
        }
    }

}
