package amtt.epam.com.amtt.util;

import android.annotation.SuppressLint;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created on 4/27/2015.
 *
 * based on https://github.com/rey5137/material/blob/master/lib/src/main/java/com/rey/material/util/ViewUtil.java
 */
public class ViewUtil {

    public static final long FRAME_DURATION = 1000 / 60;

    public static boolean hasState(int[] states, int state){
        if(states == null)
            return false;

        for (int state1 : states)
            if (state1 == state)
                return true;

        return false;
    }
}
