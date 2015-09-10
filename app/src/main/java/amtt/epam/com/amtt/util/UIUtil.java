package amtt.epam.com.amtt.util;

import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.View;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.CoreApplication;
import amtt.epam.com.amtt.database.constant.ActivityInfoConstants;

/**
 @author Artsiom_Kaliaha
 @version on 27.05.2015
 */

public final class UIUtil {

    private static final String ORIENTATION_UNDEFINED = "ORIENTATION_UNDEFINED";
    private static final String ORIENTATION_LANDSCAPE = "ORIENTATION_LANDSCAPE";
    private static final String ORIENTATION_PORTRAIT = "ORIENTATION_PORTRAIT";
    private static final Map<Integer, String> sScreenOrientation= new HashMap<>();

    static {
        sScreenOrientation.put(Configuration.ORIENTATION_UNDEFINED, ORIENTATION_UNDEFINED);
        sScreenOrientation.put(Configuration.ORIENTATION_LANDSCAPE, ORIENTATION_LANDSCAPE);
        sScreenOrientation.put(Configuration.ORIENTATION_PORTRAIT, ORIENTATION_PORTRAIT);
    }

    public static String getScreenOrientation(int orientation) {
        return sScreenOrientation.get(orientation) == null ? ActivityInfoConstants.NOT_AVAILABLE : sScreenOrientation.get(orientation);
    }
    public static DisplayMetrics getDisplayMetrics() {
        return CoreApplication.getContext().getResources().getDisplayMetrics();
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = CoreApplication.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = CoreApplication.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getOrientation() {
        return CoreApplication.getContext().getResources().getConfiguration().orientation;
    }

    public static void killApp(boolean killSafely) {
        if (killSafely) {
            /*
             * Notify the system to finalize and collect all objects of the app
             * on exit so that the virtual machine running the app can be killed
             * by the system without causing issues. NOTE: If this is set to
             * true then the virtual machine will not be killed until all of its
             * threads have closed.
             */
            System.runFinalizersOnExit(true);

            /*
             * Force the system to close the app down completely instead of
             * retaining it in the background. The virtual machine that runs the
             * app will be killed. The app will be completely created as a new
             * app in a new virtual machine running in a new process if the user
             * starts the app again.
             */
            System.exit(0);
        } else {
            /*
             * Alternatively the process that runs the virtual machine could be
             * abruptly killed. This is the quickest way to remove the app from
             * the device but it could cause problems since resources will not
             * be finalized first. For example, all threads running under the
             * process will be abruptly killed when the process is abruptly
             * killed. If one of those threads was making multiple related
             * changes to the database, then it may have committed some of those
             * changes but not all of those changes when it was abruptly killed.
             */
            android.os.Process.killProcess(android.os.Process.myPid());
        }

    }

    public static int getInDp(int px) {
        float scale = CoreApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (px * scale + 0.5f);
    }

    public static void setBackgroundCompat(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }
}
