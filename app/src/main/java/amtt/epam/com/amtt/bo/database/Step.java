package amtt.epam.com.amtt.bo.database;

import android.content.ComponentName;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.util.ActivityMetaUtil;
import amtt.epam.com.amtt.util.UIUtil;

/**
 @author Artsiom_Kaliaha
 @version on 12.05.2015
 */

public class Step extends DatabaseEntity<Step> {

    public enum ScreenshotState {

        IS_BEING_WRITTEN,
        WRITTEN

    }

    private int mStepNumber;
    private String mActivity;
    private String mScreenPath;
    private String mPackageName;
    private String mOrientation;
    private ScreenshotState mScreenState; //0 - is being written, 1 - written

    public Step() {

    }

    public Step(int stepNumber) {
        mStepNumber = stepNumber;
    }

    public Step(ComponentName componentName, String screenPath) {
        mScreenPath = screenPath;
        mOrientation = ActivityMetaUtil.getScreenOrientation(UIUtil.getOrientation());
        if (componentName != null) {
            mActivity = componentName.getClassName();
            mPackageName = componentName.getPackageName();
        }
        mScreenState = ScreenshotState.WRITTEN;
    }

    public Step(Cursor cursor) {
        super(cursor);
        mStepNumber = cursor.getInt(cursor.getColumnIndex(StepsTable._ID));
        mScreenPath = cursor.getString(cursor.getColumnIndex(StepsTable._SCREEN_PATH));
        mActivity =  cursor.getString(cursor.getColumnIndex(StepsTable._ASSOCIATED_ACTIVITY));
        mPackageName = cursor.getString(cursor.getColumnIndex(StepsTable._PACKAGE_NAME));
        mOrientation =  cursor.getString(cursor.getColumnIndex(StepsTable._ORIENTATION));
        mScreenState = cursor.getInt(cursor.getColumnIndex(StepsTable._SCREEN_STATE)) == 1 ? ScreenshotState.WRITTEN : ScreenshotState.IS_BEING_WRITTEN;
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
        values.put(StepsTable._ORIENTATION, mOrientation);
        values.put(StepsTable._SCREEN_STATE, mScreenState.ordinal());
        if (isStepWithActivityInfo()) {
            values.put(StepsTable._ASSOCIATED_ACTIVITY, mActivity);
            values.put(StepsTable._PACKAGE_NAME, mPackageName);
        }
        return values;
    }

    public String getActivity() {
        return mActivity;
    }

    public String getScreenshotPath() {
        return mScreenPath;
    }

    public String getPackageName() {
        return mPackageName;
    }

    public String getOrientation() {
        return mOrientation;
    }

    public boolean isStepWithActivityInfo() {
        return mActivity != null;
    }

    public boolean isStepWithScreenshot() {
        return mScreenPath != null;
    }

    public void setScreenshotState(ScreenshotState screenState) {
        mScreenState = screenState;
    }

    public ScreenshotState getScreenshotState() {
        return mScreenState;
    }

}