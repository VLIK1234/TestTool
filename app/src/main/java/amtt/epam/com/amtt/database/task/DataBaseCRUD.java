package amtt.epam.com.amtt.database.task;

import android.content.ContentValues;
import android.content.pm.PackageManager.NameNotFoundException;

import java.io.IOException;

import amtt.epam.com.amtt.bo.database.ActivityMeta;
import amtt.epam.com.amtt.bo.database.Step;
import amtt.epam.com.amtt.bo.issue.user.JiraUserInfo;
import amtt.epam.com.amtt.database.table.UsersTable;
import amtt.epam.com.amtt.database.task.DataBaseMethod.DatabaseMethodType;
import amtt.epam.com.amtt.processing.database.UserCheckProcessor;
import amtt.epam.com.amtt.util.ActivityMetaUtil;

/**
 * Created by Artyom on 16.05.2015.
 */
@SuppressWarnings("unchecked")
public class DataBaseCRUD {

    private static class DataBaseCRUDSingletonHolder {

        public static final DataBaseCRUD INSTANCE = new DataBaseCRUD();

    }

    public static DataBaseCRUD getInstance() {
        return DataBaseCRUDSingletonHolder.INSTANCE;
    }

    public DataBaseMethod buildStepSaving(int stepNumber) throws IOException, NameNotFoundException {
        Step step = new Step(stepNumber, ActivityMetaUtil.getTopActivityComponent());
        step.saveScreen();
        return new DataBaseMethod.Builder()
                .setMethodType(DatabaseMethodType.ADD_OR_UPDATE)
                .setEntity(step)
                .create();
    }

    public DataBaseMethod buildActivityMetaSaving() throws NameNotFoundException {
        return new DataBaseMethod.Builder()
                .setMethodType(DatabaseMethodType.ADD_OR_UPDATE)
                .setEntity(ActivityMetaUtil.createMeta())
                .create();
    }

    public DataBaseMethod buildStepCleaning() {
        return new DataBaseMethod.Builder()
                .setMethodType(DatabaseMethodType.REMOVE_ALL)
                .setEntity(new Step())
                .create();
    }

    public DataBaseMethod buildActivityMetaCleaning() {
        return new DataBaseMethod.Builder()
                .setMethodType(DatabaseMethodType.REMOVE_ALL)
                .setEntity(new ActivityMeta())
                .create();
    }

    public DataBaseMethod buildCheckUser(String userName, String url) {
        return new DataBaseMethod.Builder()
                .setMethodType(DatabaseMethodType.RAW_QUERY)
                .setEntity(new JiraUserInfo())
                .setSelection(UsersTable._USER_NAME + "=? AND " + UsersTable._URL + "=?")
                .setSelectionArgs(new String[]{userName, url})
                .setProcessor(new UserCheckProcessor())
                .create();
    }

}
