package amtt.epam.com.amtt.util;

import android.content.res.Configuration;
import android.util.DisplayMetrics;

import amtt.epam.com.amtt.AmttApplication;

/**
 @author Artsiom_Kaliaha
 @version on 27.05.2015
 */

public final class UIUtil {

    public static boolean isLandscape() {
        return AmttApplication.getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static DisplayMetrics getDisplayMetrics() {
        return AmttApplication.getContext().getResources().getDisplayMetrics();
    }

    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = AmttApplication.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = AmttApplication.getContext().getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getOrientation() {
        return AmttApplication.getContext().getResources().getConfiguration().orientation;
    }

    public static int getInDp(int px) {
        float scale = AmttApplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (px * scale + 0.5f);
    }

}
