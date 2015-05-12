package amtt.epam.com.amtt.database.dao;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;
import java.util.List;

import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.database.table.ActivityInfoTable;
import amtt.epam.com.amtt.util.ContentValuesUtil;
import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Artsiom_Kaliaha on 12.05.2015.
 */
public class StepDao implements DaoInterface<Step> {

    private static final Context sContext;

    static {
        sContext = ContextHolder.getContext();
    }

    @Override
    public int add(Step object) throws Exception {
        String screenPath = object.saveScreen();

        ComponentName activityComponent = object.getActivityComponent();
        int existingActivityInfo = sContext.getContentResolver().query(
                AmttContentProvider.ACTIVITY_META_CONTENT_URI,
                new String[]{ActivityInfoTable._ACTIVITY_NAME},
                ActivityInfoTable._ACTIVITY_NAME,
                new String[]{activityComponent.getClassName()},
                null).getCount();

        //if there is no records about current app in db
        if (existingActivityInfo == 0) {
            ActivityInfo activityInfo = sContext
                    .getPackageManager()
                    .getActivityInfo(activityComponent, PackageManager.GET_META_DATA & PackageManager.GET_INTENT_FILTERS);
            ContentValues activityContentValues = ContentValuesUtil.getValuesForActivityInfo(activityInfo);
            sContext.getContentResolver().insert(AmttContentProvider.ACTIVITY_META_CONTENT_URI, activityContentValues);
        }

        ContentValues stepContentValues = ContentValuesUtil.getValuesForStep(object.getStepNumber(), screenPath, activityComponent);
        Uri insertedStepUri = sContext.getContentResolver().insert(AmttContentProvider.STEP_CONTENT_URI, stepContentValues);
        return Integer.valueOf(insertedStepUri.getLastPathSegment());
    }

    @Override
    public void remove(Step object) throws Exception {

    }

    @Override
    public void removeByKey(int key) throws Exception {

    }

    @Override
    public void removeAll() throws Exception {
        sContext.getContentResolver().delete(AmttContentProvider.ACTIVITY_META_CONTENT_URI, null, null);
        sContext.getContentResolver().delete(AmttContentProvider.STEP_CONTENT_URI, null, null);

        File screenshotDirectory = new File(Step.SCREENSHOT_FOLDER);
        if (!screenshotDirectory.exists()) {
            screenshotDirectory.mkdir();
        } else {
            for (File screenshot : screenshotDirectory.listFiles()) {
                screenshot.delete();
            }
        }
    }

    @Override
    public void update(Step object) throws Exception {

    }

    @Override
    public List<Step> getAll() throws Exception {
        return null;
    }

    @Override
    public Step getByKey(int key) throws Exception {
        return null;
    }

}
