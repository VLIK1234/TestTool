package amtt.epam.com.amtt.database.dao;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.contentprovider.AmttContentProvider;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.table.ActivityInfoTable;
import amtt.epam.com.amtt.util.ContentValuesUtil;
import amtt.epam.com.amtt.util.ContextHolder;

/**
 * Created by Artsiom_Kaliaha on 12.05.2015.
 */
public class StepDao extends AbstractDao<Step> {

    public StepDao() {
        mUris = new Uri[] { AmttUri.STEP.get(), AmttUri.ACTIVITY_META.get() };
    }

    @Override
    List<ContentValues> getAddValues(Step object) throws Exception {
        List<ContentValues> valuesList = new ArrayList<>();

        ComponentName activityComponent = object.getActivityComponent();
        ContentValues stepContentValues = ContentValuesUtil.getValuesForStep(object.getId(), activityComponent);
        valuesList.add(stepContentValues);

        int existingActivityInfo = sContext.getContentResolver().query(
                mUris[1],
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
            valuesList.add(activityContentValues);
        }
        return valuesList;
    }

    @Override
    ContentValues getUpdateValues(Step object) throws Exception {
        return null;
    }

    @Override
    void addExtra(Step object) throws Exception {
        object.saveScreen();
    }

    @Override
    void removeAllExtra() throws Exception {
        File screenshotDirectory = new File(Step.SCREENSHOT_FOLDER);
        if (!screenshotDirectory.exists()) {
            screenshotDirectory.mkdir();
        } else {
            for (File screenshot : screenshotDirectory.listFiles()) {
                screenshot.delete();
            }
        }
    }

}
