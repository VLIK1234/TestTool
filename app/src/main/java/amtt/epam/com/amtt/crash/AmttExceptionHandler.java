package amtt.epam.com.amtt.crash;

import android.content.Context;

/**
 * Created by Artsiom_Kaliaha on 27.03.2015.
 */
public class AmttExceptionHandler implements Thread.UncaughtExceptionHandler {

    private final String mPath;

    public AmttExceptionHandler(Context context) {
        mPath = context.getCacheDir().getPath() + "/" + "crash.txt";
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        
    }
}
