package amtt.epam.com.amtt.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.TypedValue;

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

    public static float dipToPixels(Context context, float dipValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
    }

    public static float spToPixels(Context context, float spValue) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, metrics);
    }
}
