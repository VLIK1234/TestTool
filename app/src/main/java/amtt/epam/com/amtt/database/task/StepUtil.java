package amtt.epam.com.amtt.database.task;

import android.content.pm.PackageManager.NameNotFoundException;

import java.io.IOException;
import java.util.List;

import amtt.epam.com.amtt.bo.database.ActivityMeta;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.object.DbObjectManger;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.database.task.DataBaseMethod.DatabaseMethodType;
import amtt.epam.com.amtt.processing.database.UserCheckProcessor;
import amtt.epam.com.amtt.util.ActivityMetaUtil;

/**
 * Created by Artyom on 16.05.2015.
 */
public enum StepUtil {

    INSTANCE;

    public void buildStepSaving(String mScreenPath) throws IOException, NameNotFoundException {
        Step step = new Step(ActivityMetaUtil.getTopActivityComponent());
        step.saveScreen(mScreenPath);
        DbObjectManger.INSTANCE.addOrUpdateAsync(step, new IResult<Integer>() {
            @Override
            public void onResult(Integer result) {
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void buildActivityMetaSaving(ActivityMeta activityMeta){
        DbObjectManger.INSTANCE.addOrUpdateAsync(activityMeta, new IResult<Integer>() {
            @Override
            public void onResult(Integer result) {

            }

            @Override
            public void onError(Exception e) {

            }
        });
    }

    public void buildStepCleaning() {
        Step step = new Step();
        step.restartStepNumber();
        DbObjectManger.INSTANCE.removeAll(step);
    }

    public void buildActivityMetaCleaning() {
        DbObjectManger.INSTANCE.removeAll(new ActivityMeta());
    }

    public Boolean buildCheckUser(String userName) {
        final boolean[] isUserCheck = new boolean[1];
        DbObjectManger.INSTANCE.query(new JiraUserInfo(), null, UsersTable._USER_NAME, new String[]{userName}, new IResult<List<DatabaseEntity>>() {
            @Override
            public void onResult(List<DatabaseEntity> result) {
                isUserCheck[0] = result.size()>0;
            }

            @Override
            public void onError(Exception e) {

            }
        });
        return isUserCheck[0];

//        return new DataBaseMethod.Builder()
//                .setMethodType(DatabaseMethodType.RAW_QUERY)
//                .setEntity(new JiraUserInfo())
//                .setSelection(UsersTable._USER_NAME)
//                .setSelectionArgs(new String[]{userName})
//                .setProcessor(new UserCheckProcessor())
//                .create();
    }

}
