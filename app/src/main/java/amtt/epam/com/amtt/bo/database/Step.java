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
import amtt.epam.com.amtt.util.ActivityMetaUtil;
import amtt.epam.com.amtt.util.ContextHolder;
import amtt.epam.com.amtt.util.IOUtils;
import amtt.epam.com.amtt.util.UIUtil;

/**
 * Created by Artsiom_Kaliaha on 12.05.2015.
 */
public class Step extends DatabaseEntity<Step> {

    private int mStepNumber;
    private String mActivity;
    private String mScreenPath;
    private String mPackageName;
    private String mOrientation;

    public Step() {
        mScreenPath = "";
    }

    public Step(ComponentName componentName, String mScreenPath) {
        this.mScreenPath = mScreenPath;
        mActivity = componentName.getClassName();
        mPackageName = componentName.getPackageName();
        mOrientation = ActivityMetaUtil.getScreenOrientation(UIUtil.getOrientation());
    }

    public Step(Cursor cursor) {
        super(cursor);
        mStepNumber = cursor.getInt(cursor.getColumnIndex(StepsTable._ID));
        mScreenPath = cursor.getString(cursor.getColumnIndex(StepsTable._SCREEN_PATH));
        mActivity =  cursor.getString(cursor.getColumnIndex(StepsTable._ASSOCIATED_ACTIVITY));
        mPackageName = cursor.getString(cursor.getColumnIndex(StepsTable._PACKAGE_NAME));
        mOrientation =  cursor.getString(cursor.getColumnIndex(StepsTable._ORIENTATION));
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
        values.put(StepsTable._SCREEN_PATH, mScreenPath);
        values.put(StepsTable._ASSOCIATED_ACTIVITY, mActivity);
        values.put(StepsTable._PACKAGE_NAME, mPackageName);
        values.put(StepsTable._ORIENTATION, mOrientation);
        return values;
    }

    public String getActivity() {
        return mActivity;
    }

    public String getFilePath() {
        return mScreenPath;
    }

    public String getPackageName() {
        return mPackageName;
    }
    public String getOreintation() {
        return mOrientation;
    }
}