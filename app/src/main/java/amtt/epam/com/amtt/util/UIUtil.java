package amtt.epam.com.amtt.util;

import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 @author Artsiom_Kaliaha
 @version on 27.05.2015
 */

public final class UIUtil {

    public static boolean isLandscape() {
        return ContextHolder.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static DisplayMetrics getDisplayMetrics() {
        return ContextHolder.getContext().getResources().getDisplayMetrics();
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = ContextHolder.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = ContextHolder.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getOrientation() {
        return ContextHolder.getContext().getResources().getConfiguration().orientation;
    }

    public static int getInDp(int px) {
        float scale = ContextHolder.getContext().getResources().getDisplayMetrics().density;
        return (int) (px * scale + 0.5f);
    }

}
