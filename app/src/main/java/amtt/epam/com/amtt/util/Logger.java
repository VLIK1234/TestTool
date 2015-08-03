package amtt.epam.com.amtt.util;

/**
 @author Iryna Monchanka
 @version on 3/27/2015
 */

public class Logger {

    private static boolean IS_SHOW_LOGS = true;

    private Logger() {
    }

    public static void d(String tag, String message) {
        if (IS_SHOW_LOGS) {
            android.util.Log.d(tag, message);
        }
    }

    public static void d(String tag, String message, Throwable throwable) {
        if (IS_SHOW_LOGS) {
            android.util.Log.d(tag, message, throwable);
        }
    }

    public static void i(String tag, String message) {
        if (IS_SHOW_LOGS) {
            android.util.Log.i(tag, message);
        }
    }

    public static void i(String tag, String message, Throwable throwable) {
        if (IS_SHOW_LOGS) {
            android.util.Log.i(tag, message, throwable);
        }
    }

    public static void e(String tag, String message) {
        if (IS_SHOW_LOGS) {
            android.util.Log.e(tag, message);
        }
    }

    public static void e(String tag, String message, Throwable throwable) {
        if (IS_SHOW_LOGS) {
            android.util.Log.e(tag, message, throwable);
        }
    }

    public static void v(String tag, String message) {
        if (IS_SHOW_LOGS) {
            android.util.Log.v(tag, message);
        }
    }

    public static void v(String tag, String message, Throwable throwable) {
        if (IS_SHOW_LOGS) {
            android.util.Log.v(tag, message, throwable);
        }
    }

    public static void w(String tag, Throwable throwable) {
        if (IS_SHOW_LOGS) {
            android.util.Log.w(tag, throwable);
        }
    }

    public static void w(String tag, String message, Throwable throwable) {
        if (IS_SHOW_LOGS) {
            android.util.Log.w(tag, message, throwable);
        }
    }

    public static void loggerOn(){
        IS_SHOW_LOGS = true;
    }
    public static void loggerOff(){
        IS_SHOW_LOGS = false;
    }
}
