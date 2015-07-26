package com.example.ivan_bakach.testpermission;

import android.content.Context;

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
    public static final String ROTATE_LOG = " -r 512";
    public static final String EXCEPTION_FILTER = " *:e";
    public static final String TEMPLATE_EXCEPION = "%s/log_exception.txt";
    public static final String TEMPLATE_COMMON = "%s/log_common.txt";
    public static final String TEMPLATE_ARGUMENTS_FRAGMENTS = "%s/log_arguments.txt";

    public static void writeMultipleLogs(Context context) {
        File externalCacheDir = context.getCacheDir();
        File logCacheDir = new File(externalCacheDir, LOGS_CACHE_DIR);
        logCacheDir.mkdir();
        sArgumentsFragments = String.format(TEMPLATE_ARGUMENTS_FRAGMENTS, logCacheDir.getPath());
        sExceptionLog = String.format(TEMPLATE_EXCEPION, logCacheDir.getPath());
        sCommonLog = String.format(TEMPLATE_COMMON, logCacheDir.getPath());
        IOUtils.deleteFileIfExist(sArgumentsFragments);
        IOUtils.deleteFileIfExist(sExceptionLog);
        IOUtils.deleteFileIfExist(sCommonLog);
        try {
            Runtime.getRuntime().exec(LOGCAT_WRITE_IN_FILE + sExceptionLog + ROTATE_LOG + EXCEPTION_FILTER);//write exception log
            Runtime.getRuntime().exec(LOGCAT_WRITE_IN_FILE + sCommonLog + ROTATE_LOG);//write all log
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
