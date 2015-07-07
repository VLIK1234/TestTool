package com.example.ivan_bakach.testpermission;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
                deleteFileIfExist(sCommonLog);
                closeUnitTest = true;
                break;
        }
    }

    public static void writeMultipleLogs(Context context) {
        String templateException = "%s/log_exception.txt";
        String templateCommon = "%s/log_common.txt";
        File externalCache = context.getExternalCacheDir();
        externalCache.mkdirs();
        sExceptionLog = String.format(templateException, externalCache.getPath());
        sCommonLog = String.format(templateCommon, externalCache.getPath());
        deleteFileIfExist(sExceptionLog);
        deleteFileIfExist(sCommonLog);
        try {
            Runtime.getRuntime().exec("logcat -c");
            Runtime.getRuntime().exec("logcat -f " + sExceptionLog + " *:w");
            Runtime.getRuntime().exec("logcat -f " + sCommonLog);
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