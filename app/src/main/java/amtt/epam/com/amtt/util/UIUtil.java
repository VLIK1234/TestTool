package amtt.epam.com.amtt.util;

import android.content.Context;
import android.content.res.Configuration;

/**
 * Created by Artsiom_Kaliaha on 27.05.2015.
 */
public class UIUtil {

    private static final Context sContext;

    static {
        sContext = ContextHolder.getContext();
    }

    public static boolean isLandscape() {
        return sContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

}
