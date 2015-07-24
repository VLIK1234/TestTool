package com.example.ivan_bakach.testpermission;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Ivan_Bakach on 09.07.2015.
 */
public class LogManger {

    public static String sArgumentsFragments;
    public static String sExceptionLog;
    public static String sCommonLog;
    public static final String LOGCAT_WRITE_IN_FILE = "logcat -f ";
    public static final String EXCEPTION_FILTER = " *:e";
    public static final String TEMPLATE_EXCEPION = "%s/log_exception.txt";
    public static final String TEMPLATE_COMMON = "%s/log_common.txt";
    public static final String TEMPLATE_ARGUMENTS_FRAGMENTS = "%s/log_arguments.txt";

    public static void writeMultipleLogs(Context context) {
        File EXTERNAL_CACHE = context.getFilesDir();
        sArgumentsFragments = String.format(TEMPLATE_ARGUMENTS_FRAGMENTS, EXTERNAL_CACHE.getPath());
        sExceptionLog = String.format(TEMPLATE_EXCEPION, EXTERNAL_CACHE.getPath());
        sCommonLog = String.format(TEMPLATE_COMMON, EXTERNAL_CACHE.getPath());
        IOUtils.deleteFileIfExist(sArgumentsFragments);
        IOUtils.deleteFileIfExist(sExceptionLog);
        IOUtils.deleteFileIfExist(sCommonLog);
        try {
            Runtime.getRuntime().exec(LOGCAT_WRITE_IN_FILE + sExceptionLog + EXCEPTION_FILTER);//write exception log
            Runtime.getRuntime().exec(LOGCAT_WRITE_IN_FILE + sCommonLog);//write all log
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
