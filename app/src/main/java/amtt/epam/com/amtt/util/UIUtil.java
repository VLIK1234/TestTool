package amtt.epam.com.amtt.util;

import android.content.res.Configuration;
import android.util.DisplayMetrics;

import java.util.HashMap;
import java.util.Map;

import amtt.epam.com.amtt.AmttApplication;
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
