package com.example.ivan_bakach.testpermission;

import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ivan_Bakach on 09.07.2015.
 */
public class LogManger {

    public static final String LOGS_CACHE_DIR = "logs";
    public static String sArgumentsFragments;
    public static String sExceptionLog;
    public static String sCommonLog;
    public static final String LOGCAT_WRITE_IN_FILE = "logcat -f ";
    public static final String ROTATE_LOG = " -r 1000";
    public static final String EXCEPTION_FILTER = " *:e";
    public static final String TEMPLATE_EXCEPION = "%s/log_exception.txt";
    public static final String TEMPLATE_COMMON = "%s/log_common.txt";
    public static final String TEMPLATE_ARGUMENTS_FRAGMENTS = "%s/log_arguments.txt";

    public static final String FILE_NAME_KEY = "fileName";
    public static final String BYTE_ARRAY_DATA_KEY = "byteArrayData";
    public static final String SEND_LOG_FILE_ACTION = "SEND_LOG_FILE";
    public static final String CAT_ALL_FILE_ACTION = "CAT_ALL_FILE";
    private static Process sProcessWriteCommonLog;
    private static Process sProcessWriteExceptionLog;

    public static void writeMultipleLogs(Context context) {
        File externalCacheDir = context.getCacheDir();
        File logCacheDir = new File(externalCacheDir, LOGS_CACHE_DIR);
        logCacheDir.mkdir();
        sArgumentsFragments = String.format(TEMPLATE_ARGUMENTS_FRAGMENTS, logCacheDir.getPath());
        sExceptionLog = String.format(TEMPLATE_EXCEPION, logCacheDir.getPath());
        sCommonLog = String.format(TEMPLATE_COMMON, logCacheDir.getPath());
        IOUtils.deleteFiles(logCacheDir.listFiles());
        try {
            sProcessWriteCommonLog = Runtime.getRuntime().exec(LOGCAT_WRITE_IN_FILE + sExceptionLog + ROTATE_LOG + EXCEPTION_FILTER);//write exception log
            sProcessWriteExceptionLog = Runtime.getRuntime().exec(LOGCAT_WRITE_IN_FILE + sCommonLog + ROTATE_LOG);//write all log
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void appendMultipleLogs(Context context) {
        File externalCacheDir = context.getCacheDir();
        File logCacheDir = new File(externalCacheDir, LOGS_CACHE_DIR);
        logCacheDir.mkdir();
        sArgumentsFragments = String.format(TEMPLATE_ARGUMENTS_FRAGMENTS, logCacheDir.getPath());
        sExceptionLog = String.format(TEMPLATE_EXCEPION, logCacheDir.getPath());
        sCommonLog = String.format(TEMPLATE_COMMON, logCacheDir.getPath());
        try {
            sProcessWriteCommonLog = Runtime.getRuntime().exec(LOGCAT_WRITE_IN_FILE + sExceptionLog + ROTATE_LOG + EXCEPTION_FILTER);//write exception log
            sProcessWriteExceptionLog = Runtime.getRuntime().exec(LOGCAT_WRITE_IN_FILE + sCommonLog + ROTATE_LOG);//write all log
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void closeLogsWriter() {
        if (sProcessWriteCommonLog!=null) {
            sProcessWriteCommonLog.destroy();
        }
        if (sProcessWriteExceptionLog!=null) {
            sProcessWriteExceptionLog.destroy();
        }
    }

    public static void transferLogsToAmtt(Context context){
        File externalCacheDir = context.getCacheDir();
        File logCacheDir = new File(externalCacheDir, LogManger.LOGS_CACHE_DIR);
        File[] listFile = IOUtils.getAllFileInFolder(logCacheDir);
        for (File file : listFile) {
            Intent intentLogs = new Intent();
            intentLogs.setAction(SEND_LOG_FILE_ACTION);
            intentLogs.putExtra(FILE_NAME_KEY, file.getName());
            try {
                intentLogs.putExtra(BYTE_ARRAY_DATA_KEY, IOUtils.toByteArray(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            context.sendOrderedBroadcast(intentLogs, null);
        }
    }

}
