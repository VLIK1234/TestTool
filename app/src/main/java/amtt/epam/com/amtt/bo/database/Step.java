package amtt.epam.com.amtt.bo.database;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import amtt.epam.com.amtt.contentprovider.AmttUri;
import amtt.epam.com.amtt.database.dao.DatabaseEntity;
import amtt.epam.com.amtt.database.table.StepsTable;
import amtt.epam.com.amtt.util.ContextHolder;
import amtt.epam.com.amtt.util.IOUtils;

/**
 * Created by Artsiom_Kaliaha on 12.05.2015.
 */
public class Step extends DatabaseEntity {

    private static final String SCREENSHOT_COMMAND = "/system/bin/screencap -p ";
    private static final String CHANGE_PERMISSION_COMMAND = "chmod 777 ";
    public static final String SCREENSHOT_FOLDER = "/screenshot/";

    private static final String sScreenBasePath;
    private int mStepNumber;
    private ComponentName mActivity;
    private String mScreenPath;

    static {
        sScreenBasePath = ContextHolder.getContext().getFilesDir().getPath() + SCREENSHOT_FOLDER;
    }

    public Step() { }

    public Step(int stepNumber, ComponentName componentName) {
        mStepNumber = stepNumber;
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

    @Override
    public String getWhere() {
        return StepsTable._ID;
    }

    public String getScreenPath() {
        return mScreenPath;
    }

    //help methods
    public String saveScreen() throws IOException {
        String screenPath = null;
        Process fileSavingProcess = null;
        Process changeModeProcess = null;
        OutputStream fileSavingStream = null;
        OutputStream changeModeStream = null;
        try {
            fileSavingProcess = Runtime.getRuntime().exec("su");
            fileSavingStream = fileSavingProcess.getOutputStream();
            fileSavingStream.write((SCREENSHOT_COMMAND + (mScreenPath = sScreenBasePath + "screen" + mStepNumber + ".png")).getBytes("ASCII"));
            fileSavingStream.flush();
            fileSavingStream.close();

            changeModeProcess = Runtime.getRuntime().exec("su");
            changeModeStream = changeModeProcess.getOutputStream();
            changeModeStream.write((CHANGE_PERMISSION_COMMAND + mScreenPath + "\n").getBytes("ASCII"));
            changeModeStream.flush();
            changeModeStream.close();
            changeModeProcess.destroy();
        } finally {
            IOUtils.close(fileSavingStream, changeModeStream);
            IOUtils.destroyProcesses(fileSavingProcess, changeModeProcess);
        }
        return screenPath;
    }

}
