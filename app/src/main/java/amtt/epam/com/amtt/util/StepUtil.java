package amtt.epam.com.amtt.util;

import android.content.pm.PackageManager.NameNotFoundException;

import java.io.IOException;
import amtt.epam.com.amtt.bo.database.ActivityMeta;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.bo.user.JUserInfo;
import amtt.epam.com.amtt.database.object.DbObjectManger;
import amtt.epam.com.amtt.database.object.IResult;
import amtt.epam.com.amtt.database.table.UsersTable;

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
        return DbObjectManger.INSTANCE.query(new JUserInfo(), null, UsersTable._USER_NAME, new String[]{userName}).size()>0;
    }

}
