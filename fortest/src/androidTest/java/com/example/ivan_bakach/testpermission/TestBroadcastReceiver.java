package com.example.ivan_bakach.testpermission;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ivan_Bakach on 29.06.2015.
 */
public class TestBroadcastReceiver extends BroadcastReceiver {

    public static final String PING_ANSWER = "PING_ANSWER";
    public static final String PING_REQUEST = "PING_REQUEST";
    public static final String CLOSE_TEST = "CLOSE_TEST";
    public static final String CATEGORY = "android.intent.category.DEFAULT";
    private boolean closeUnitTest;
    private static String sExceptionLog;
    private static String sWarningLog;
    private static String sCommonLog;

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()) {
            case PING_REQUEST:
                Intent in = new Intent();
                in.putExtra(PING_ANSWER, "Success answer");
                context.sendBroadcast(in);
                break;
            case CLOSE_TEST:
                deleteFileIfExist(sExceptionLog);
                deleteFileIfExist(sWarningLog);
                deleteFileIfExist(sCommonLog);
                closeUnitTest = true;
                break;
        }
    }

    public static void writeMultipleLogs() {
        String templateExcepion = "%s/log_exception.txt";
        String templateWarning = "%s/log_warning.txt";
        String templateCommon = "%s/log_common.txt";
        File externalCache = new File(Environment.getExternalStorageDirectory(),"Amtt_cache");
        externalCache.mkdirs();
        sExceptionLog = String.format(templateExcepion, externalCache.getPath());
        sWarningLog = String.format(templateWarning, externalCache.getPath());
        sCommonLog = String.format(templateCommon, externalCache.getPath());
        deleteFileIfExist(sExceptionLog);
        deleteFileIfExist(sWarningLog);
        deleteFileIfExist(sCommonLog);
        try {
            Runtime.getRuntime().exec("logcat -c");//clear log history
            Runtime.getRuntime().exec("logcat -f " + sExceptionLog + " *:e");//write exception log
            Runtime.getRuntime().exec("logcat -f " + sWarningLog + " *:w");//write exception and warning log
            Runtime.getRuntime().exec("logcat -f " + sCommonLog);//write all log
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteFileIfExist(String filePath) {
        File myFile = new File(filePath);
        if (myFile.exists()) {
            myFile.delete();
        }
    }

    public boolean getCloseUnitTest() {
        return closeUnitTest;
    }

    public void setCloseUnitTest(boolean value) {
        closeUnitTest = value;
    }
}