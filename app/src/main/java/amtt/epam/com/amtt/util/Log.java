package amtt.epam.com.amtt.util;

/**
 * Created by Iryna_Monchanka on 4/3/2015.
 */
public final class Log {

    private static final Boolean IS_SHOW_LOGS = true;

    private Log() {
    }

    public static void d(String mNameClass, String mMessage) {
        if (IS_SHOW_LOGS) {
            android.util.Log.d(mNameClass, mMessage);
        }
    }

    public static void i(String mNameClass, String mMessage) {
        if (IS_SHOW_LOGS) {
            android.util.Log.i(mNameClass, mMessage);
        }
    }


}