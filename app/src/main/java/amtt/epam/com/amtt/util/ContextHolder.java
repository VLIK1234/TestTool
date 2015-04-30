package amtt.epam.com.amtt.util;

import android.content.Context;

/**
 * Created by Ivan_Bakach on 15.04.2015.
 */
public class ContextHolder {

    private static Context context;

    public ContextHolder(Context base) {
        context = base;
    }

    public static void setContext(Context context){
        ContextHolder.context = context;
    }

    public static Context getContext(){
        return ContextHolder.context;
    }
}
