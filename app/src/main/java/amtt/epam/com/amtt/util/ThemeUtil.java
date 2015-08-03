package amtt.epam.com.amtt.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.TypedValue;

import amtt.epam.com.amtt.R;

/**
 * Created on 4/27/2015.
 * based on https://github.com/rey5137/material/blob/master/lib/src/main/java/com/rey/material/util/ThemeUtil.java
 */
public class ThemeUtil {

    private static final String TAG = ThemeUtil.class.getSimpleName();
    private static TypedValue value;

    private static int getColor(Context context, int id, int defaultValue) {
        if (value == null)
            value = new TypedValue();

        try {
            Resources.Theme theme = context.getTheme();
            if (theme != null && theme.resolveAttribute(id, value, true)) {
                if (value.type >= TypedValue.TYPE_FIRST_INT && value.type <= TypedValue.TYPE_LAST_INT)
                    return value.data;
                else if (value.type == TypedValue.TYPE_STRING)
                    return context.getResources().getColor(value.resourceId);
            }
        } catch (Exception ex) {
            Logger.e(TAG, ex.getMessage(), ex);
        }

        return defaultValue;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static int colorControlNormal(Context context, int defaultValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return getColor(context, android.R.attr.colorControlNormal, defaultValue);

        return getColor(context, R.attr.colorControlNormal, defaultValue);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static int colorControlActivated(Context context, int defaultValue) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            return getColor(context, android.R.attr.colorControlActivated, defaultValue);

        return getColor(context, R.attr.colorControlActivated, defaultValue);
    }
}
