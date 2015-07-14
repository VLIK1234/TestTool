package com.example.ivan_bakach.testpermission;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ivan_Bakach on 09.07.2015.
 */
public class LogManger {

    public static String sExceptionLog;
    public static String sWarningLog;
    public static String sCommonLog;

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

    public static void deleteFileIfExist(String filePath) {
        File myFile = new File(filePath);
        if (myFile.exists()) {
            myFile.delete();
        }
    }
}
