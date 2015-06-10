package amtt.epam.com.amtt.bo.database;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;

import java.io.IOException;
import java.io.OutputStream;

import amtt.epam.com.amtt.app.LoginActivity;
import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.util.ContextHolder;
import amtt.epam.com.amtt.util.IOUtils;

/**
 * Created by Artsiom_Kaliaha on 12.05.2015.
 */
public class Step extends DatabaseEntity<Step> {

    private static int mStepNumber = 0;
    private String mActivity;
    private String mScreenPath;

    public Step() {
        mScreenPath = "";
    }

    public Step(ComponentName componentName, String mScreenPath) {
        mActivity = componentName.getClassName();
        this.mScreenPath = mScreenPath;
        mStepNumber++;
    }

    public Step(String componentName, String mScreenPath) {
        mActivity = componentName;
        this.mScreenPath = mScreenPath;
        mStepNumber++;
    }

    public Step(Cursor cursor) {
        super(cursor);
        mStepNumber = cursor.getInt(cursor.getColumnIndex(StepsTable._ID));
        mScreenPath = cursor.getString(cursor.getColumnIndex(StepsTable._SCREEN_PATH));
        mActivity =  cursor.getString(cursor.getColumnIndex(StepsTable._ASSOCIATED_ACTIVITY));
    }

    @Override
    public int getId() {
        return mStepNumber;
    }

    @Override
    public Uri getUri() {
        return AmttUri.STEP.get();
    }

    @Override
    public Step parse(Cursor cursor) {
        return new Step(cursor);
    }

    @Override
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(StepsTable._ID, mStepNumber);
        values.put(StepsTable._SCREEN_PATH, mScreenPath);
        values.put(StepsTable._ASSOCIATED_ACTIVITY, mActivity);
        return values;
    }

    public String getActivity() {
        return mActivity;
    }

    public String getScreenPath() {
        return mScreenPath;
    }

    public static void restartStepNumber(){
        mStepNumber = 0;
    }

}