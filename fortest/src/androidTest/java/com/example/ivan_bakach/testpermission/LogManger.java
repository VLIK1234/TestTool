package com.example.ivan_bakach.testpermission;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ivan_Bakach on 09.07.2015.
 */
public class LogManger {

    public static final String AMTT_CACHE_DIRECTORY = "Amtt_cache";
    public static String sExceptionLog;
    public static String sCommonLog;
    public static final String LOGCAT_WRITE_IN_FILE = "logcat -f ";
    public static final String EXCEPTION_FILTER = " *:e";
    public static final String TEMPLATE_EXCEPION = "%s/log_exception.txt";
    public static final String TEMPLATE_COMMON = "%s/log_common.txt";

    public static void writeMultipleLogs() {
        File externalCache = new File(Environment.getExternalStorageDirectory(), AMTT_CACHE_DIRECTORY);
        externalCache.mkdirs();
        sExceptionLog = String.format(TEMPLATE_EXCEPION, externalCache.getPath());
        sCommonLog = String.format(TEMPLATE_COMMON, externalCache.getPath());
        deleteFileIfExist(sExceptionLog);
        deleteFileIfExist(sCommonLog);
        try {
            Runtime.getRuntime().exec(LOGCAT_WRITE_IN_FILE + sExceptionLog + EXCEPTION_FILTER);//write exception log
            Runtime.getRuntime().exec(LOGCAT_WRITE_IN_FILE + sCommonLog);//write all log
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteFileIfExist(String filePath) {
        File myFile = new File(filePath);
        if (myFile.exists()) {
            myFile.delete();
        }
    }
}
