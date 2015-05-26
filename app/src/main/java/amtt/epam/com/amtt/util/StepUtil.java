package amtt.epam.com.amtt.util;

import android.content.ComponentName;
import android.content.pm.PackageManager.NameNotFoundException;

import java.io.IOException;
import java.util.List;

import amtt.epam.com.amtt.bo.database.ActivityMeta;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbObjectManger;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.database.table.UsersTable;

/**
 * Created by Artyom on 16.05.2015.
 */
public class StepUtil {

    public static void buildStepSaving(ComponentName componentName, String mScreenPath){
        Step step = new Step(componentName, mScreenPath);
        DbObjectManger.INSTANCE.addOrUpdateAsync(step, null);
    }

    public static void buildActivityMetaSaving(ActivityMeta activityMeta){
        DbObjectManger.INSTANCE.addOrUpdateAsync(activityMeta, null);
    }

    public static void buildStepCleaning() {
        Step.restartStepNumber();
        DbObjectManger.INSTANCE.removeAll(new Step());
    }

    public static void buildActivityMetaCleaning() {
        DbObjectManger.INSTANCE.removeAll(new ActivityMeta());
    }

    public static boolean buildCheckUser(String userName) {
        final boolean[] returnResult = new boolean[1];
        DbObjectManger.INSTANCE.query(new JUserInfo(), null, new String[]{UsersTable._USER_NAME}, new String[]{userName}, new IResult<List<DatabaseEntity>>() {
            @Override
            public void onResult(List<DatabaseEntity> result) {
                returnResult[0] = result.size() > 0;
            }

            @Override
            public void onError(Exception e) {

            }
        });
        return returnResult[0];
    }

}
