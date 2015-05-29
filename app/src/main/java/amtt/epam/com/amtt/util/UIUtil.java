package amtt.epam.com.amtt.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * Created by Artsiom_Kaliaha on 27.05.2015.
 */
public final class UIUtil {

    private static final Context sContext;

    static {
        sContext = ContextHolder.getContext();
    }

    public static boolean isLandscape() {
        return sContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static DisplayMetrics getDisplayMetrics() {
        return sContext.getResources().getDisplayMetrics();
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = sContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = sContext.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getOrientation() {
        return sContext.getResources().getConfiguration().orientation;
    }

}
