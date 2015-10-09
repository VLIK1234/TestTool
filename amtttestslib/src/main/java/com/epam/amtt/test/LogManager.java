package com.epam.amtt.test;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 @author Ivan_Bakach
 @version on 09.07.2015
 */

public class LogManager {

    private static final String TAG = LogManager.class.getSimpleName();
    private static final String LOGS_CACHE_DIR = "logs";
    private static final String LOGCAT_CLEAR = "logcat -c";
    private static final String LOGCAT_WRITE_IN_FILE = "logcat -f ";
    private static final String ROTATE_LOG = " -r 512";
    private static final String FORMAT_LOG = " -v time";
    private static final String EXCEPTION_FILTER = " *:e";
    private static final String TEMPLATE_EXCEPTION = "%s/log_exception.txt";
    private static final String TEMPLATE_COMMON = "%s/log_common.txt";
    private static final String TEMPLATE_ARGUMENTS_FRAGMENTS = "%s/log_arguments.txt";
    private static final String FILE_NAME_KEY = "fileName";
    private static final String BYTE_ARRAY_DATA_KEY = "byteArrayData";
    private static final String SEND_LOG_FILE_ACTION = "SEND_LOG_FILE";
    private static Process sProcessClearLog;
    private static Process sProcessWriteCommonLog;
    private static Process sProcessWriteExceptionLog;
    public static String sArgumentsFragments;
    public static String sExceptionLog;
    public static String sCommonLog;

    public static void writeMultipleLogs(Context context) {
        File internalCacheDir = context.getCacheDir();
        File logCacheDir = new File(internalCacheDir, LOGS_CACHE_DIR);
        logCacheDir.mkdir();
        sArgumentsFragments = String.format(TEMPLATE_ARGUMENTS_FRAGMENTS, logCacheDir.getPath());
        sExceptionLog = String.format(TEMPLATE_EXCEPTION, logCacheDir.getPath());
        sCommonLog = String.format(TEMPLATE_COMMON, logCacheDir.getPath());
        IOUtils.deleteFiles(logCacheDir.listFiles());
        try {
            sProcessClearLog = Runtime.getRuntime().exec(LOGCAT_CLEAR);//clear log
            sProcessWriteCommonLog = Runtime.getRuntime().exec(LOGCAT_WRITE_IN_FILE + sExceptionLog + ROTATE_LOG + FORMAT_LOG + EXCEPTION_FILTER);//write exception log
            sProcessWriteExceptionLog = Runtime.getRuntime().exec(LOGCAT_WRITE_IN_FILE + sCommonLog + ROTATE_LOG + FORMAT_LOG);//write all log
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static void closeLogsWriter() {
        if (sProcessClearLog!=null) {
            sProcessClearLog.destroy();
        }
        if (sProcessWriteCommonLog!=null) {
            sProcessWriteCommonLog.destroy();
        }
        if (sProcessWriteExceptionLog!=null) {
            sProcessWriteExceptionLog.destroy();
        }
    }

    public static void transferLogsToAmtt(Context context){
        File externalCacheDir = context.getCacheDir();
        File logCacheDir = new File(externalCacheDir, LogManager.LOGS_CACHE_DIR);
        File[] listFile = IOUtils.getAllFileInFolder(logCacheDir);
        for (File file : listFile) {
            Intent intentLogs = new Intent();
            intentLogs.setAction(SEND_LOG_FILE_ACTION);
            intentLogs.putExtra(FILE_NAME_KEY, file.getName());
            try {
                intentLogs.putExtra(BYTE_ARRAY_DATA_KEY, IOUtils.toByteArray(file));
            } catch (IOException e) {
                Log.e(TAG, e.getMessage(), e);
            }
            context.sendOrderedBroadcast(intentLogs, null);
        }
    }

}
