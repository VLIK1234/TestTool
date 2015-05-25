package amtt.epam.com.amtt.bo.database;

import android.content.ComponentName;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.io.IOException;
import java.io.OutputStream;

import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.object.DatabaseEntity;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.util.ContextHolder;
import amtt.epam.com.amtt.util.IOUtils;

/**
 * Created by Artsiom_Kaliaha on 12.05.2015.
 */
public class Step extends DatabaseEntity {

    private static int mStepNumber = 0;
    private ComponentName mActivity;
    private String mScreenPath;

    public Step() {
    }

    public Step(ComponentName componentName) {
        mActivity = componentName;
    }

    public Step(Cursor cursor) {
        super(cursor);
        mStepNumber = cursor.getInt(cursor.getColumnIndex(StepsTable._ID));
        mScreenPath = cursor.getString(cursor.getColumnIndex(StepsTable._SCREEN_PATH));
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
    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(StepsTable._ID, mStepNumber);
        values.put(StepsTable._SCREEN_PATH, mScreenPath);
        values.put(StepsTable._ASSOCIATED_ACTIVITY, mActivity.getClassName());
        return values;
    }

    public String getScreenPath() {
        return mScreenPath;
    }

    public void restartStepNumber(){
        mStepNumber = 0;
    }

    //help methods
    public void saveScreen(String mScreenPath) throws IOException {
        this.mScreenPath = mScreenPath;
        mStepNumber++;
    }

}